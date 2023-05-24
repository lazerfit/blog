package com.blog.web.controller;

import com.blog.service.PostsService;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.dto.PostsSaveRequestDto;
import com.blog.web.dto.PostsSearchRequestDto;
import com.blog.web.dto.PostsUpdateRequestDto;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PostsController {

    private final PostsService postsService;

    @PostMapping("/posts")
    public Long save(@Valid @RequestBody PostsSaveRequestDto request) {
        return postsService.save(request);
    }

    @GetMapping("/posts")
    public List<PostsResponseDto> getPostsList(PostsSearchRequestDto request) {
        return postsService.getPostsList(request);
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
