package RandomChatting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//ํ-๋งคํ
@Controller
public class IndexController {
    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("/index")
    public String index(){return "index";}
}
