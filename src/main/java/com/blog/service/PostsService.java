package com.blog.service;

import com.blog.domain.posts.Posts;
import com.blog.domain.posts.PostsEditor;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.PostsNotFound;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.dto.PostsSaveRequestDto;
import com.blog.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto request) {
        return postsRepository.save(request.toEntity()).getId();
    }

    @Transactional
    public void edit(Long id, PostsUpdateRequestDto request) {
        Posts posts = postsRepository.findById(id).orElseThrow(PostsNotFound::new);

        PostsEditor.PostsEditorBuilder editorBuilder=posts.toEditor();
        PostsEditor postsEditor = editorBuilder.title(request.title())
            .content(request.content())
            .build();

        posts.edit(postsEditor);
    }

    public PostsResponseDto findById(Long id) {
        Posts posts = postsRepository.findById(id)
            .orElseThrow(PostsNotFound::new);
        return new PostsResponseDto(posts);
    }

    public Page<PostsResponseDto> getPostsList(Pageable pageable) {
        return postsRepository.getPostsList(pageable);
    }

    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(PostsNotFound::new);
        postsRepository.delete(posts);
    }
}


