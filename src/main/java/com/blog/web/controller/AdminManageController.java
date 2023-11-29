package com.blog.web.controller;

import com.blog.domain.user.UserRepository;
import com.blog.service.UserService;
import com.blog.service.CategoryService;
import com.blog.web.dto.UserPasswordEditRequest;
import com.blog.web.dto.UserResponse;
import com.blog.web.dto.category.CategoryEditRequest;
import com.blog.web.dto.category.CategoryResponse;
import com.blog.web.dto.category.CategorySaveRequest;
import com.blog.web.form.CategoryEditForm;
import com.blog.web.form.CategoryForm;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class AdminManageController {

    private final CategoryService categoryService;
    private final UserService userService;
    private static final String REDIRECT_HOME_URL = "redirect:/admin/setting/category";
    private final UserRepository userRepository;

    @GetMapping("/admin/setting/category")
    public String manageCategory(Model model) {
        List<CategoryResponse> allCategory = categoryService.findAllCategory();
        model.addAttribute("allCategory", allCategory);

        model.addAttribute("categoryForm", new CategoryForm());
        model.addAttribute("categoryEditForm", new CategoryEditForm());
        return "form/manageCategoryForm";
    }

    @PostMapping("/admin/setting/category")
    public String saveCategory(CategoryForm categoryForm) {
        CategorySaveRequest categorySaveRequest =
            new CategorySaveRequest(categoryForm.getTitle(), categoryForm.getListOrder());
        categoryService.save(categorySaveRequest);
        return REDIRECT_HOME_URL;
    }

    @PostMapping("/admin/setting/category/delete/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
        return REDIRECT_HOME_URL;
    }

    @PostMapping("/admin/setting/category/edit/{categoryId}")
    public String editCategory(@RequestBody @Validated CategoryEditForm editForm,
        @PathVariable Long categoryId) {
        CategoryEditRequest editRequest =
            CategoryEditRequest.builder()
                .title(editForm.getTitle())
                .listOrder(editForm.getListOrder())
                .build();
        categoryService.edit(categoryId, editRequest);
        return REDIRECT_HOME_URL;
    }

    @GetMapping("/admin/setting/account")
    public String getAllAccount(Model model) {
        List<UserResponse> allUsers = userService.findAllUser();
        model.addAttribute("allUsers", allUsers);

        return "adminUserSetting";
    }

    @PostMapping("/admin/setting/account/edit/password")
    @ResponseBody
    public void editPassword(@RequestBody @Valid UserPasswordEditRequest passwordEdit) {
        userService.changePassword(passwordEdit);
    }
}