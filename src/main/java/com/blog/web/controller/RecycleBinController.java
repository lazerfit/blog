package com.blog.web.controller;

import com.blog.service.CategoryService;
import com.blog.service.PostsService;
import com.blog.service.RecycleBinService;
import com.blog.web.dto.RecycleBinRestore;
import com.blog.web.dto.category.CategoryResponse;
import com.blog.web.dto.posts.PostSaveRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
@RestController
@Slf4j
public class RecycleBinController {

    private final RecycleBinService recycleBinService;
    private final PostsService postsService;
    private final CategoryService categoryService;

    @PostMapping("/admin/setting/recycleBin/delete/{postId}")
    public ResponseEntity<String> deleteRecycleBinPost(@PathVariable Long postId) {
        recycleBinService.delete(postId);

        return new ResponseEntity<>("삭제 완료", HttpStatus.OK);
    }

    @PostMapping("/recycleBin/restore")
    public void restorePost(@Valid @RequestBody RecycleBinRestore restore) {

        CategoryResponse category = categoryService.findCategoryByTitle(
            restore.getCategory());

        postsService.save(PostSaveRequest.builder()
            .title(restore.getTitle())
            .content(restore.getContent())
            .tags(restore.getTags())
            .category(category.toEntity())
            .views(restore.getViews())
            .thumbnail(restore.getThumbnail())
            .build());
    }
}
