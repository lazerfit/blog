package com.blog.web.controller;

import com.blog.service.CategoryService;
import com.blog.service.UserService;
import com.blog.web.dto.category.CategoryEditRequest;
import com.blog.web.dto.user.UserPasswordEditRequest;
import com.blog.web.form.CategoryEditForm;
import com.blog.web.form.UserRoleEditForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
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
    private static final String REDIRECT_HOME_URL = "redirect:/admin/setting";

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
                .id(editForm.getId())
                .build();
        categoryService.edit(editRequest);
        return REDIRECT_HOME_URL;
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
        return new ResponseEntity<>("수정이 완료되었습니다.",HttpStatus.OK);
    }
}
