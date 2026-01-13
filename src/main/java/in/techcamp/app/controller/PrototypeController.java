package in.techcamp.app.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.techcamp.app.entity.PrototypeEntity;
import in.techcamp.app.repository.PrototypeRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeController {
  private final PrototypeRepository prototypeRepository;

  @GetMapping("/")
  public String showPrototypes(Model model) {
    List<PrototypeEntity> prototypes = prototypeRepository.findAll();
    model.addAttribute("prototypes", prototypes);
    return "prototype/index";
  }
}
