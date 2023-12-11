package com.blog.web.controller;

import com.blog.service.AuthService;
import com.blog.web.dto.SiteUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PreAuthorize("permitAll()")
    @GetMapping("/login")
    public String login() {
        return "form/login";
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/auth/signup")
    public String signUp(@RequestBody @Valid SiteUserDto siteUserDto) {
        authService.signUp(siteUserDto);
        return "redirect:/";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/admin/setting")
    public String adminManagePage() {
        return "adminSetting";
    }
}
