package in.techcamp.app.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import in.techcamp.app.ImageUrl;
import in.techcamp.app.custom_user.CustomUserDetail;
import in.techcamp.app.entity.PrototypeEntity;
import in.techcamp.app.form.PrototypeForm;
import in.techcamp.app.repository.PrototypeRepository;
import in.techcamp.app.repository.UserRepository;
import in.techcamp.app.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeController {
  private final PrototypeRepository prototypeRepository;
  private final UserRepository userRepository;
  private final ImageUrl imageUrl;

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

  PrototypeEntity prototype = new PrototypeEntity();
    prototype.setUser(userRepository.findByUserId(currentUser.getUserId()));
    prototype.setPrototypeName(prototypeForm.getPrototypeName());
    prototype.setConcept(prototypeForm.getConcept());
    prototype.setCatchCopy(prototypeForm.getCatchCopy());

  MultipartFile imageFile = prototypeForm.getImage();
    if (imageFile != null && !imageFile.isEmpty()) {
      try {
        String uploadDir = imageUrl.getImageUrl();

        Path uploadPath = Paths.get(imageUrl.getImageUrl()).toAbsolutePath().normalize();
        System.out.println("画像の保存ディレクトリの絶対パス： " + uploadPath);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir, fileName);

        Files.copy(imageFile.getInputStream(), imagePath);
        prototype.setImage("/uploads/" + fileName);
      } catch (IOException e) {
        System.out.println("エラー：" + e);
        return "prototype/new";
      }
    }

    try {
      prototypeRepository.insert(prototype);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "prototype/new";
    }

    return "redirect:/";
  }

  @GetMapping("/prototype/{prototypeId}/edit")
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

    model.addAttribute("prototypeForm", new PrototypeForm());
    model.addAttribute("prototypeId", prototypeId);
    return "prototype/edit";
  }

  @PostMapping("/prototype/{prototypeId}/edit")
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

  PrototypeEntity prototype = prototypeRepository.findById(prototypeId);
    prototype.setUser(userRepository.findByUserId(currentUser.getUserId()));
    prototype.setPrototypeName(prototypeForm.getPrototypeName());
    prototype.setConcept(prototypeForm.getConcept());
    prototype.setCatchCopy(prototypeForm.getCatchCopy());

  MultipartFile imageFile = prototypeForm.getImage();
    if (imageFile != null && !imageFile.isEmpty()) {
      try {
        String uploadDir = imageUrl.getImageUrl();
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir, fileName);
        Files.copy(imageFile.getInputStream(), imagePath);
        prototype.setImage("/uploads/" + fileName);
      } catch (IOException e) {
        System.out.println("エラー：" + e);
        return "prototype/edit";
      }
    }

    try {
      prototypeRepository.update(prototype);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "prototype/edit";
    }

    return "prototype/{prototypeId}";
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

}
