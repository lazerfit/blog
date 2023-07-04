package com.blog.web.controller;

import com.blog.domain.category.Category;
import com.blog.service.CategoryService;
import com.blog.service.PostsService;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.dto.PostsResponseWithCategoryDto;
import com.blog.web.dto.PostsSaveRequestDto;
import com.blog.web.dto.PostsUpdateRequestDto;
import com.blog.web.form.CreatePostsForm;
import com.blog.web.form.EditPostsForm;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostsController {

    private final PostsService postsService;

    private final CategoryService categoryService;

    @GetMapping("/posts/new")
    public String createPostsForm(Model model) {
        List<Category> allCategories = categoryService.findAllCategory();
        model.addAttribute("createPostsForm", new CreatePostsForm());
        model.addAttribute("allCategories", allCategories);
        return "form/createPostsForm";
    }

    @PostMapping("/posts/new")
    public String save(@Valid CreatePostsForm form) {
        Category getCategoryByTitle = categoryService.getCategoryByTitle(form.getCategoryTitle());
        PostsSaveRequestDto request = new PostsSaveRequestDto(form.getTitle(), form.getContent(),
            getCategoryByTitle);
        postsService.save(request);
        return "redirect:/";
    }

    @GetMapping("/posts/edit/{postId}")
    public String editForm(@PathVariable Long postId, Model model) {
        PostsResponseWithCategoryDto originalPosts = postsService.findByIdWithCategory(postId);
        EditPostsForm editPostsForm = new EditPostsForm(originalPosts.getTitle(),
            originalPosts.getContent(),originalPosts.getCategoryTitle());
        List<Category> allCategories = categoryService.findAllCategory();
        model.addAttribute("editPostsForm", editPostsForm);
        model.addAttribute("allCategories", allCategories);
        return "form/editPostsForm";
    }


    @PostMapping("/posts/edit/{postId}")
    public String edit(@PathVariable Long postId, @RequestBody @Valid EditPostsForm form) {
        Category categoryByTitle = categoryService.getCategoryByTitle(form.getCategoryTitle());
        PostsUpdateRequestDto request = new PostsUpdateRequestDto(form.getTitle(),
            form.getContent(),categoryByTitle);
        postsService.edit(postId, request);
        return "redirect:/posts/{postId}";
    }

    @GetMapping("/posts/{postId}")
    public String findById(@PathVariable Long postId, Model model) {
        PostsResponseWithCategoryDto postFindById = postsService.findByIdWithCategory(postId);
        List<Category> allCategorizedPosts = categoryService.findAllCategory();
        model.addAttribute("postFindById", postFindById);
        model.addAttribute("allCategorizedPosts", allCategorizedPosts);
        return "posts";
    }

    @PostMapping("/posts/delete/{postId}")
    public String delete(@PathVariable Long postId) {
        postsService.delete(postId);
        return "redirect:/";
    }

    @GetMapping("/posts/search")
    public String searchPostsByKeyword(Pageable pageable, Model model, @RequestParam String q) {
        Page<PostsResponseDto> searchedPostsListByKeyword = postsService.getSearchedPostsListByKeyword(
            pageable, q);
        model.addAttribute("postsList", searchedPostsListByKeyword);
        model.addAttribute("keyword", q);
        return "index";
    }

    @GetMapping("/posts/category")
    public String getCategorizedPosts(Pageable pageable, @RequestParam String q,
        Model model) {
        Page<PostsResponseWithCategoryDto> categorizedPosts = postsService.getCategorizedPosts(
            pageable, q);
        List<Category> allCategorizedPosts = categoryService.findAllCategory();
        model.addAttribute("categorizedPosts", categorizedPosts);
        model.addAttribute("keyword", q);
        model.addAttribute("allCategorizedPosts", allCategorizedPosts);
        return "categorizedPosts";
    }
}
