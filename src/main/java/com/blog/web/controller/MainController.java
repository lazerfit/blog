package com.blog.web.controller;

import com.blog.service.CategoryService;
import com.blog.service.PostsService;
import com.blog.web.dto.posts.PostsIndexContent;
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
        Page<PostsResponse> posts = postsService.getPosts(pageable);
        List<PostsIndexContent> postsIndexContents = posts.stream()
            .map(r -> PostsIndexContent.builder()
                .title(r.getTitle())
                .id(r.getId())
                .thumbnail(r.getThumbnail())
                .content(postsService.getContentPlainText(r.getContent()))
                .createdDate(r.getCreatedDate())
                .build()).toList();

        model.addAttribute("postsList", posts);
        model.addAttribute("postsListPlainText", postsIndexContents);

        //sidebar
        var allCategoryAndPostCreatedDate = categoryService.getAllCategoryAndPostCreatedDate();
        model.addAttribute("sidebarCategory", allCategoryAndPostCreatedDate);

        List<PostsResponse> popularPosts = postsService.getPopularPosts();
        model.addAttribute("popularPosts", popularPosts);
        return "index";
    }
}
