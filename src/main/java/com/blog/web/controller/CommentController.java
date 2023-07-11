package com.blog.web.controller;

import com.blog.service.CommentService;
import com.blog.web.form.CommentForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    @PostMapping("/posts/{postId}/comment")
    public String insertComment(@PathVariable Long postId,@RequestBody @Valid CommentForm form) {

        commentService.save(postId,form);

        return "redirect:/posts/{postId}";
    }
}
