package com.blog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorResponseController {

    @GetMapping("/403")
    public String httpStatus403() {
        return "error/403";
    }

    @GetMapping("/401")
    public String httpStatus401() {
        return "error/401";
    }
}
