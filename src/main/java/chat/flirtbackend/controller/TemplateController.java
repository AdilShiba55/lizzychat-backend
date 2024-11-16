package chat.flirtbackend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.context.Context;

@Controller
@RequestMapping("/template")
public class TemplateController {

    @GetMapping("/send-verify-code")
    public String sendVerifyCode(Model model) {
        model.addAttribute("code", 123456);
        return "send-verify-code";
    }

}
