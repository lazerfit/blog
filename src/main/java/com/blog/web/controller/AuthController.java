package com.blog.web.controller;

import com.blog.service.AuthService;
import com.blog.web.dto.SiteUserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping("/login")
    public String login() {
        return "form/loginForm";
    }

    @PostMapping("/auth/signup")
    public void signUp(@RequestBody SiteUserDto siteUserDto) {
        authService.signUp(siteUserDto);
    }

    @GetMapping("/admin/setting")
    public String adminManagePage() {
        return "adminSetting";
    }
}
