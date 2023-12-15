package com.blog.web.controller;

import com.blog.service.AuthService;
import com.blog.service.CategoryService;
import com.blog.service.UserService;
import com.blog.web.dto.category.CategoryResponse;
import com.blog.web.dto.user.SiteUserDto;
import com.blog.web.dto.user.UserResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final CategoryService categoryService;
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
    public String adminManagePage(Model model) {
        List<UserResponse> allUsers = userService.findAllUser();
        model.addAttribute("allUsers", allUsers);

        List<CategoryResponse> allCategory = categoryService.findAllCategory();
        model.addAttribute("allCategory", allCategory);
        return "admin/adminPage";
    }
}
