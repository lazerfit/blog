package com.blog.web.controller;

import com.blog.domain.category.Category;
import com.blog.service.CategoryService;
import com.blog.service.PostsService;
import com.blog.web.dto.PostsResponseWithoutCommentDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
@Slf4j
@Controller
public class HomeController {

    private final PostsService postsService;

    private final CategoryService categoryService;

    @GetMapping("/")
    public String index(Pageable pageable, Model model) {
        Page<PostsResponseWithoutCommentDto> posts = postsService.getPosts(pageable);
        model.addAttribute("postsList", posts);

        // 조회수 상위 3개 게시물 가져오기
        List<PostsResponseWithoutCommentDto> popularPosts = postsService.getPopularPosts();
        model.addAttribute("popularPosts",popularPosts);
        return "index";
    }

    @ModelAttribute
    public void addCategory(Model model) {
        List<Category> allCategory = categoryService.findAllCategory();
        model.addAttribute("allCategorizedPosts", allCategory);
    }
}

