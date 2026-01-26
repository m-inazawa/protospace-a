package in.techcamp.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.techcamp.app.custom_user.CustomUserDetail;
import in.techcamp.app.entity.PrototypeEntity;
import in.techcamp.app.form.PrototypeForm;
import in.techcamp.app.repository.PrototypeRepository;
import in.techcamp.app.service.PrototypeService;
import in.techcamp.app.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeController {
  private final PrototypeRepository prototypeRepository;
  private final PrototypeService prototypeService;

  @GetMapping("/")
  public String showPrototypes(@RequestParam(name = "sort", defaultValue = "desc") String sort,
                               @RequestParam(name = "keyword", required = false) String keyword, //必須ではない(required=false)
                               @RequestParam(name = "fromDate", required = false) String fromDate,
                               @RequestParam(name = "toDate", required = false) String toDate,
                               @AuthenticationPrincipal CustomUserDetail currentUser,
                               Model model) {
    String start = (fromDate == null || fromDate.isEmpty()) ? null : fromDate + " 00:00:00"; // 日付をSQL用に変更
    String end = (toDate == null || toDate.isEmpty()) ? null : toDate + " 23:59:59";
    String order = "asc".equals(sort) ? "ASC" : "DESC"; //ascならASC、それ以外はすべてDESC
    List<PrototypeEntity> prototypes = prototypeRepository.findAllWithFilters(order, keyword, start, end);
    model.addAttribute("prototypes", prototypes);
    model.addAttribute("sort",sort); //選択した順番で画面を維持
    model.addAttribute("keyword", keyword); // 入力した内容を画面維持
    model.addAttribute("fromDate", fromDate);
    model.addAttribute("toDate", toDate);

    if (currentUser != null) {
      model.addAttribute("userName", currentUser.getLoginUserName());
    }
    return "prototype/index";
  }

  @GetMapping("prototype/new")
  public String showPrototypeNew(Model model) {
    model.addAttribute("prototypeForm", new PrototypeForm());
    return "prototype/new";
  }
  
  @PostMapping("prototype/new")
  public String createPrototype(@ModelAttribute("prototypeForm") @Validated(ValidationOrder.class) PrototypeForm prototypeForm,BindingResult result, 
                                @AuthenticationPrincipal CustomUserDetail currentUser,Model model) {
      
    if (result.hasErrors()) {
      List<String> errorMessages = result.getAllErrors().stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              .collect(Collectors.toList());
      model.addAttribute("errorMessages", errorMessages);
      model.addAttribute("prototypeForm", prototypeForm);
      return "prototype/new";
    }

    try {
      prototypeService.createPrototype(prototypeForm, currentUser);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "prototype/new";
    }

    return "redirect:/";
  }

  @GetMapping("prototype/{prototypeId}/edit")
  public String showPrototypeEdit(@PathVariable("prototypeId") Integer prototypeId,
                                  @AuthenticationPrincipal CustomUserDetail currentUser,Model model) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);

    if (currentUser == null || !prototype.getUser().getId().equals(currentUser.getUserId())) {
        return "redirect:/";
    }

    PrototypeForm prototypeForm = new PrototypeForm();
    prototypeForm.setPrototypeName(prototype.getPrototypeName());
    prototypeForm.setConcept(prototype.getConcept());
    prototypeForm.setCatchCopy(prototype.getCatchCopy());
    prototypeForm.setVersion(prototype.getVersion());

    model.addAttribute("prototypeForm", prototypeForm);
    model.addAttribute("prototypeId", prototypeId);
    return "prototype/edit";
  }

  @PostMapping("prototype/{prototypeId}/edit")
  public String updatePrototype(@ModelAttribute("prototypeForm") @Validated(ValidationOrder.class) PrototypeForm prototypeForm,BindingResult result, 
                                @PathVariable("prototypeId") Integer prototypeId,
                                @AuthenticationPrincipal CustomUserDetail currentUser,Model model) {
      
    if (result.hasErrors()) {
      List<String> errorMessages = result.getAllErrors().stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              .collect(Collectors.toList());
      model.addAttribute("errorMessages", errorMessages);
      model.addAttribute("prototypeForm", prototypeForm);
      return "prototype/edit";
    }

    try {
      prototypeService.updatePrototype(prototypeForm, currentUser, prototypeId);
    } 

    catch (OptimisticLockingFailureException e) { //楽観ロックエラー
        model.addAttribute("conflictError", "他のユーザーがこの投稿を更新しました。最新の状態を確認してから、再度入力してください。");
        model.addAttribute("prototypeForm", prototypeForm); // 現在の入力内容を保持
        return "prototype/edit";
    }
    
    catch (Exception e) {
      System.out.println("エラー：" + e);
      return "prototype/edit";
    }

    return "redirect:/prototype/" + prototypeId;
  }

@GetMapping("/prototype/{prototypeId}")
public String showPrototypeDetail(@PathVariable("prototypeId") Integer prototypeId, Model model) {
PrototypeEntity prototype = prototypeRepository.findById(prototypeId);

if (prototype == null) {
    return "redirect:/"; 
}

model.addAttribute("prototype", prototype);
model.addAttribute("comments", prototype.getComments());
return "prototype/detail";
}

  @PostMapping("/prototype/{prototypeId}/delete")
  public String deletePrototype(@PathVariable("prototypeId") Integer prototypeId,
                                @AuthenticationPrincipal CustomUserDetail currentUser) {
    PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    
    if (currentUser == null || !prototype.getUser().getId().equals(currentUser.getUserId())) {
        return "redirect:/";
    }

    try {
      prototypeRepository.deleteById(prototypeId);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return  "redirect:/";
    }
    return "redirect:/";
  } 
}
