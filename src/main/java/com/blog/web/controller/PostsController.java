package com.blog.web.controller;

import com.blog.service.PostsService;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.dto.PostsSaveRequestDto;
import com.blog.web.dto.PostsUpdateRequestDto;
import com.blog.web.form.CreatePostsForm;
import com.blog.web.form.EditPostsForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/posts/edit/{postId}")
    public String editForm(@PathVariable Long postId,Model model) {
        PostsResponseDto originalPosts = postsService.findById(postId);
        EditPostsForm editPostsForm = new EditPostsForm(originalPosts.getTitle(),
            originalPosts.getContent());
        model.addAttribute("editPostsForm", editPostsForm);
        return "form/editPostsForm";
    }


    @PostMapping("/posts/edit/{postId}")
    public String edit(@PathVariable Long postId, @Valid EditPostsForm form) {
        PostsUpdateRequestDto request = new PostsUpdateRequestDto(form.getTitle(),
            form.getContent());
        postsService.edit(postId, request);
        return "redirect:/posts/{postId}";
    }

    @GetMapping("/posts/{postId}")
    public String findById(@PathVariable Long postId,Model model) {

        PostsResponseDto postFindById = postsService.findById(postId);
        model.addAttribute("postFindById",postFindById);
        return "posts";
    }

    @PostMapping("/posts/delete/{postId}")
    public void delete(@PathVariable Long postId) {
        postsService.delete(postId);
//        return "redirect:/";
    }
}
