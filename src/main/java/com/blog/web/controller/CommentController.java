package com.blog.web.controller;

import com.blog.service.CommentService;
import com.blog.service.PostsService;
import com.blog.web.dto.CommentEditRequest;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.form.CommentEditForm;
import com.blog.web.form.CommentForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final PostsService postsService;

    @PostMapping("/posts/comment/new")
    public String insertComment(@RequestBody @Valid CommentForm form,
        Model model) {

        commentService.save(form.getPostId(), form);
        PostsResponseDto response = postsService.getPostsById(form.getPostId());
        model.addAttribute("postFindById", response);

        return "posts :: commentList";
    }

    @PostMapping("/posts/comment/delete")
    public String delete(@RequestParam Long commentId, @RequestParam Long postId, Model model) {
        commentService.delete(commentId);
        PostsResponseDto response = postsService.getPostsById(postId);
        model.addAttribute("postFindById", response);

        return "posts :: commentList";
    }

    @PostMapping("/posts/{postId}/comment/edit")
    public String edit(@PathVariable Long postId,CommentEditForm form,Model model) {
        CommentEditRequest commentEditRequest = new CommentEditRequest(form);
        commentService.edit(commentEditRequest);
        PostsResponseDto response = postsService.getPostsById(postId);
        model.addAttribute("postFindById", response);

        return "posts :: commentList";
    }
}
