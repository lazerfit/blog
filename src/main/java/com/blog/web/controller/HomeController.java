package com.blog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(RedirectAttributes re) {
        re.addAttribute("page", 1);
        re.addAttribute("size", 6);
        return "redirect:/posts";
    }
}
