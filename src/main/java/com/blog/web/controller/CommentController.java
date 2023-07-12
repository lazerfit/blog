package com.blog.web.controller;

import com.blog.service.CommentService;
import com.blog.service.PostsService;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.form.CommentForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final PostsService postsService;

    @PostMapping("/posts/comment/{postId}")
    public String insertComment(@PathVariable Long postId,@RequestBody @Valid CommentForm form,
        Model model) {

        commentService.save(postId,form);
        PostsResponseDto response = postsService.getPostsById(form.getPostId());
        model.addAttribute("postFindById", response);

        return "posts :: commentList";
    }
}
