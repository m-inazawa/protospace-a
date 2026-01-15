package in.techcamp.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.validation.BindingResult;

import in.techcamp.app.form.RegisterForm;
import in.techcamp.app.service.UserService;
import in.techcamp.app.validation.ValidationOrder;
import in.techcamp.app.validation.ValidationPriority1;
import in.techcamp.app.validation.ValidationPriority2;


import in.techcamp.app.entity.UserEntity;
import in.techcamp.app.form.RegisterForm;
import in.techcamp.app.repository.UserRepository;
import in.techcamp.app.service.UserService;
import in.techcamp.app.validation.ValidationOrder;
import lombok.AllArgsConstructor;



@Controller
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {
  private final UserService userService;
  private final UserRepository userRepository;

  @GetMapping("/login")
  public String showLogin(@RequestParam(value = "error", required = false) String error, Model model) {
    if (error != null) {
      model.addAttribute("loginError", "invalid email or password.");
    }
    return ("users/login");
  }

  @GetMapping("/register")
  public String showUserForm(Model model) {
    model.addAttribute("registerForm", new RegisterForm());
    return ("users/register");
  }

  @PostMapping("/register")
  public String registerUser(@ModelAttribute("userForm") @Validated({ValidationOrder.class}) RegisterForm userForm, BindingResult result, Model model) {
    try {
      userService.createUserWithEncryptedPassword(userForm, result);
      if (result.hasErrors()) {
        System.out.println("★Error発生時のメッセージ内容" + result );
        model.addAttribute("registerError", result);
        return "/users/register";
      }
    } catch (Exception e) {
      model.addAttribute("registerError", "システムエラーにより操作を完了できませんでした。");
      System.out.println("★通信エラー：" + e);
      return "/users/register";
    }
      
    return "redirect:/users/login";
  }

  @GetMapping("/{userId}")
  public String showUserDetail(@PathVariable("userId") Integer userId, Model model) {
    UserEntity userEntity = userRepository.findByUserId(userId);
    model.addAttribute("user", userEntity);
    return "users/detail";
  }
}
