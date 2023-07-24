package com.blog.web.controller;

import com.blog.domain.category.Category;
import com.blog.service.CategoryService;
import com.blog.web.dto.category.CategoryEditRequest;
import com.blog.web.dto.category.CategorySaveRequest;
import com.blog.web.form.CategoryEditForm;
import com.blog.web.form.CategoryForm;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/setting/category")
public class AdminCategoryManageController {

    private final CategoryService categoryService;
    private static final String REDIRECT_HOME_URL="redirect:/admin/setting/category";

    @GetMapping("")
    public String manageCategory(Model model) {
        addAllCategoryAttribute(model);
        addCategoryFromAndCategoryEditForm(model);
        return "form/manageCategoryForm";
    }

    @PostMapping("")
    public String saveCategory(CategoryForm categoryForm) {
        CategorySaveRequest categorySaveRequest = createCategorySaveRequest(categoryForm);
        categoryService.save(categorySaveRequest);
        return REDIRECT_HOME_URL;
    }

    @PostMapping("/delete/{categoryId}")
    public String deleteCategory(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
        return REDIRECT_HOME_URL;
    }

    @PostMapping("/edit/{categoryId}")
    public String editCategory(@RequestBody @Valid CategoryEditForm editForm,@PathVariable Long categoryId) {
        CategoryEditRequest editRequest = createCategoryEditRequest(editForm);
        categoryService.edit(categoryId,editRequest);
        return REDIRECT_HOME_URL;
    }

    //Method
    private void addAllCategoryAttribute(Model model) {
        List<Category> allCategory = categoryService.findAllCategory();
        model.addAttribute("allCategory", allCategory);
    }

    private void addCategoryFromAndCategoryEditForm(Model model) {
        model.addAttribute("categoryForm", new CategoryForm());
        model.addAttribute("categoryEditForm", new CategoryEditForm());
    }

    private CategorySaveRequest createCategorySaveRequest(CategoryForm categoryForm) {
        return new CategorySaveRequest(categoryForm.getTitle(), categoryForm.getListOrder());
    }

    private CategoryEditRequest createCategoryEditRequest(CategoryEditForm editForm) {
        return CategoryEditRequest.builder()
            .title(editForm.getTitle())
            .listOrder(editForm.getListOrder())
            .build();
    }
}
