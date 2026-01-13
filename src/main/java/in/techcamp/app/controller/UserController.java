package in.techcamp.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/users")
public class UserController {

  @GetMapping("/login")
  public String showSignUp(@RequestParam(value = "error", required = false) String error, Model model) {
    if (error != null) {
      model.addAttribute("loginError", "invalid email or password.");
    }
    return ("users/login");
  }


  // @GetMapping("/signUp")
  // public String showSignUp(@RequestBody Model model) {
  //   return ("protType/index");
  // }  
}
