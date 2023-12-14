package com.blog.web.controller;

import com.blog.service.CategoryService;
import com.blog.service.UserService;
import com.blog.web.dto.category.CategoryEditRequest;
import com.blog.web.dto.category.CategoryResponse;
import com.blog.web.dto.category.CategorySaveRequest;
import com.blog.web.dto.user.UserPasswordEditRequest;
import com.blog.web.dto.user.UserResponse;
import com.blog.web.form.CategoryEditForm;
import com.blog.web.form.CategoryForm;
import com.blog.web.form.UserRoleEditForm;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/admin/setting/category/edit")
    public String editCategory(@RequestBody @Validated CategoryEditForm editForm) {
        CategoryEditRequest editRequest =
            CategoryEditRequest.builder()
                .title(editForm.getTitle())
                .listOrder(editForm.getListOrder())
                .build();
        categoryService.edit(editRequest);
        return "redirect:/admin/setting";
    }

    @GetMapping("/admin/setting/account")
    public String getAllAccount(Model model) {
        List<UserResponse> allUsers = userService.findAllUser();
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("roleEditForm", new UserRoleEditForm());
        return "admin/adminUserSetting";
    }

    @PostMapping("/admin/setting/account/edit/password")
    @ResponseBody
    public void editPassword(@RequestBody @Valid UserPasswordEditRequest passwordEdit) {
        userService.changePassword(passwordEdit);
    }

    @PostMapping("/admin/setting/account/edit/role")
    @ResponseBody
    public ResponseEntity<String> editRole(@RequestBody @Valid UserRoleEditForm form) {
        userService.editRole(form);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
