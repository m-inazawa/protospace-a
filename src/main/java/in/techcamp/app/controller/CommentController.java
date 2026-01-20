package in.techcamp.app.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.techcamp.app.custom_user.CustomUserDetail;
import in.techcamp.app.entity.CommentEntity;
import in.techcamp.app.entity.PrototypeEntity;
import in.techcamp.app.form.CommentForm;
import in.techcamp.app.repository.CommentRepository;
import in.techcamp.app.repository.PrototypeRepository;
import in.techcamp.app.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class CommentController {
  private final CommentRepository commentRepository;
  private final PrototypeRepository prototypeRepository;
  
  @PostMapping("/prototype/{prototypeId}/comments")
  public String createComment(
    @PathVariable("prototypeId") Integer prototypeId,
    @ModelAttribute("commentForm") @Validated(ValidationOrder.class) CommentForm commentForm,
    BindingResult result,
    @AuthenticationPrincipal CustomUserDetail currentUser,
    Model model
  ) {
    if (result.hasErrors()) {
      List<String> errorMessages = result.getAllErrors().stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .collect(Collectors.toList());
      
      PrototypeEntity prototype = prototypeRepository.findById(prototypeId);

      model.addAttribute("prototype", prototype);
      model.addAttribute("comments", prototype.getComments());
      model.addAttribute("errorMessages", errorMessages);
      return "prototype/detail";
    }
    
    CommentEntity commentEntity = new CommentEntity();
    commentEntity.setPrototypeId(prototypeId);
    commentEntity.setUserId(currentUser.getUserId());
    commentEntity.setComment(commentForm.getComment());
    
    try {
      commentRepository.insert(commentEntity);
    } catch (Exception e) {
      model.addAttribute("errorMessages", "システムエラーにより操作を完了できませんでした。");
      System.out.println("エラー：" + e);
      return "prototype/detail";
    }

    return "redirect:/prototype/" + prototypeId;

  }
}
