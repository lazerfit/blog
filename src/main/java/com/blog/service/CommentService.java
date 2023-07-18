package com.blog.service;

import com.blog.domain.comments.Comment;
import com.blog.domain.comments.CommentsRepository;
import com.blog.domain.posts.Post;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CommentNotFound;
import com.blog.exception.PostsNotFound;
import com.blog.web.dto.CommentEditRequest;
import com.blog.web.dto.CommentPassword;
import com.blog.web.dto.CommentsResponseDto;
import com.blog.web.dto.CommentsSaveRequestDto;
import com.blog.web.form.CommentForm;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentsRepository commentsRepository;

    private final PostsRepository postsRepository;

    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void save(Long postId,CommentForm form) {
        Post post = postsRepository.findById(postId).orElseThrow(PostsNotFound::new);
        Comment comment = commentsRepository.findById(form.getParentId()).orElse(null);
        CommentsSaveRequestDto request = new CommentsSaveRequestDto(
            form.getUsername(), form.getContent(), comment, post, passwordEncoder.encode(
            form.getPassword()));
        commentsRepository.save(request.toEntity());
    }

    public CommentsResponseDto findById(Long id) {

        Comment comment = commentsRepository.findById(id).orElseThrow(CommentNotFound::new);

        return new CommentsResponseDto(comment);
    }

    public CommentPassword findPassword(Long id) {
        Comment comment = commentsRepository.findById(id).orElseThrow(CommentNotFound::new);
        return new CommentPassword(comment.getPassword());
    }

    public List<CommentsResponseDto> findByPostsId(Long postsId) {
        return commentsRepository.findByPostId(postsId).stream().map(CommentsResponseDto::new).toList();
    }

    public void delete(Long commentId) {
        Comment comment = commentsRepository.findById(commentId).orElseThrow(CommentNotFound::new);
        commentsRepository.delete(comment);
    }

    @Transactional
    public void edit(CommentEditRequest request) {
        Comment comment = commentsRepository.findById(request.getId()).orElseThrow(CommentNotFound::new);
        comment.edit(request.getContent());
    }
}
