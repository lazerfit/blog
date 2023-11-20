package com.blog.web.controller;

import com.blog.service.CategoryService;
import com.blog.service.PostsService;
import com.blog.web.dto.category.CategoryResponse;
import com.blog.web.dto.posts.PostsResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final PostsService postsService;
    private final CategoryService categoryService;

    @PreAuthorize("permitAll()")
    @GetMapping("/")
    public String index(Pageable pageable, Model model) {
        Page<PostsResponse> posts = postsService.getPostsExcludingComment(pageable);
        model.addAttribute("postsList", posts);
        populateRelatedSidebar(model);
        return "index";
    }

    //common method
    private void populateRelatedSidebar(Model model) {
        addCategoriesAttributes(model);
        addPopularPostsAttributes(model);
    }

    private void addCategoriesAttributes(Model model) {
        List<CategoryResponse> allCategory = categoryService.findAllCategory();
        model.addAttribute("allCategorizedPosts", allCategory);
    }

    private void addPopularPostsAttributes(Model model) {
        List<PostsResponse> popularPosts = postsService.getPopularPosts();
        model.addAttribute("popularPosts",popularPosts);
    }
}
