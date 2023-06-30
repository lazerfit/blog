package com.blog.web.controller;

import com.blog.service.CategoryService;
import com.blog.web.dto.CategoryCreateRequestDto;
import com.blog.web.form.CategoryForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/admin/setting/category")
    public String manageCategory(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        return "form/manageCategoryForm";
    }

    @PostMapping("/admin/setting/category")
    public String createCategory(CategoryForm categoryForm) {
        CategoryCreateRequestDto categoryCreateRequestDto = new CategoryCreateRequestDto(
            categoryForm.getTitle(),
            categoryForm.getListOrder());

        categoryService.save(categoryCreateRequestDto);

        return "redirect:/admin/setting";
    }

}
