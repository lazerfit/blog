package com.blog.web.controller;

import com.blog.service.PostsService;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.dto.PostsSaveRequestDto;
import com.blog.web.dto.PostsSearchRequestDto;
import com.blog.web.dto.PostsUpdateRequestDto;
import com.blog.web.form.CreatePostsForm;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostsController {

    private final PostsService postsService;

    @GetMapping("/posts/new")
    public String createPostsForm(Model model) {
        model.addAttribute("createPostsForm", new CreatePostsForm());
        return "form/createPostsForm";
    }

    @PostMapping("/posts/new")
    public String save(@Valid CreatePostsForm form) {
        PostsSaveRequestDto request = new PostsSaveRequestDto(form.getTitle(), form.getContent());
        postsService.save(request);
        return "redirect:/";
    }

    @GetMapping("/posts")
    public String getPostsList(@ModelAttribute PostsSearchRequestDto request,Model model) {
        List<PostsResponseDto> postsList = postsService.getPostsList(request);
        model.addAttribute("postsLists",postsList);
        return "index";
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody PostsUpdateRequestDto request) {
        postsService.edit(postId, request);
    }

    @GetMapping("/posts/{postId}")
    public PostsResponseDto findById(@PathVariable Long postId) {
        return postsService.findById(postId);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postsService.delete(postId);
    }
}
