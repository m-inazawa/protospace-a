package in.techcamp.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.techcamp.app.custom_user.CustomUserDetail;
import in.techcamp.app.entity.PrototypeEntity;
import in.techcamp.app.form.PrototypeForm;
import in.techcamp.app.repository.PrototypeRepository;
import in.techcamp.app.repository.UserRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeController {
  private final PrototypeRepository prototypeRepository;
  private final UserRepository userRepository;

  @GetMapping("/")
  public String showPrototypes(Model model) {
    List<PrototypeEntity> prototypes = prototypeRepository.findAll();
    model.addAttribute("prototypes", prototypes);
    return "prototype/index";
  }

  @GetMapping("prototype/new")
  public String showPrototypeNew(Model model) {
    model.addAttribute("prototypeForm", new PrototypeForm());
    return "prototype/new";
  }
  
  @PostMapping("prototype/new")
  public String createPrototype(@ModelAttribute("prototypeForm") @Validated PrototypeForm prototypeForm,BindingResult result, 
                                @AuthenticationPrincipal CustomUserDetail currentUser,Model model) {
      
    if (result.hasErrors()) {
      List<String> errorMessages = result.getAllErrors().stream()
              .map(DefaultMessageSourceResolvable::getDefaultMessage)
              .collect(Collectors.toList());
      model.addAttribute("errorMessages", errorMessages);
      model.addAttribute("prototypeForm", prototypeForm);
      return "/login";
  }

  PrototypeEntity prototype = new PrototypeEntity();
    prototype.setUser(userRepository.findByUserId(currentUser.getUserId()));
    prototype.setPrototypeName(prototypeForm.getPrototypeName());
    prototype.setConcept(prototypeForm.getConcept());
    prototype.setCatchCopy(prototypeForm.getCatchCopy());
    prototype.setImage(prototypeForm.getImage());

    try {
      prototypeRepository.insert(prototype);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "prototype/new";
    }

    return "redirect:/";
  }
}