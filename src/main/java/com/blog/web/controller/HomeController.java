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
import org.springframework.web.bind.annotation.ResponseBody;

@RequiredArgsConstructor
@Slf4j
@Controller
public class HomeController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Pageable pageable, Model model) {
        Page<PostsResponseDto> postsList = postsService.getPostsList(pageable);
        model.addAttribute("postsList", postsList);
        return "index";
    }

    @GetMapping("/user")
    @ResponseBody
    public String user() {
        return "사용자 페이지입니다.";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "관리자 페이지입니다.";
    }
}

