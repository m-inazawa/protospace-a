//画面遷移・API（キャラデータ取得など）
package in.techcamp.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MascotController {

    @GetMapping("/mascot/index")
    public String mascotIndex() {
        // src/main/resources/templates/index.html を表示
        return "mascot/index";
    }

}
