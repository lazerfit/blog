package com.blog.web.controller;

import com.blog.service.PostsService;
import com.blog.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Slf4j
@Controller
public class HomeController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Pageable pageable, Model model) {
        Page<PostsResponseDto> postsList = postsService.getPostsList(pageable);
        model.addAttribute("postsList",postsList);
        return "index";
    }
}

