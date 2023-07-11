package com.blog.service;

import com.blog.domain.comments.Comment;
import com.blog.domain.comments.CommentsRepository;
import com.blog.domain.posts.Posts;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CommentNotFound;
import com.blog.exception.PostsNotFound;
import com.blog.web.dto.CommentsResponseDto;
import com.blog.web.dto.CommentsSaveRequestDto;
import com.blog.web.form.CommentForm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentsRepository commentsRepository;

    private final PostsRepository postsRepository;

    @Transactional
    public void save(Long postId,CommentForm form) {
        Posts post = postsRepository.findById(postId).orElseThrow(PostsNotFound::new);
        Comment comment = commentsRepository.findById(form.getParentId()).orElse(null);
        CommentsSaveRequestDto request = new CommentsSaveRequestDto(
            form.getUsername(), form.getContent(), comment, post);
        commentsRepository.save(request.toEntity());
    }

    public CommentsResponseDto findById(Long id) {

        Comment comment = commentsRepository.findById(id).orElseThrow(CommentNotFound::new);

        return new CommentsResponseDto(comment);
    }
}
