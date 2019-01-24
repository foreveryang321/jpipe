package top.ylonline.jpipe.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author YL
 */
@Controller
@RequestMapping("/index")
public class IndexController {

    @GetMapping
    public String index(Model model) {
        model.addAttribute("id", "123456");
        model.addAttribute("name", "foreveryang321");
        return "index";
    }
}
