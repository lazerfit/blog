package com.blog.service;

import com.blog.domain.comments.Comment;
import com.blog.domain.comments.CommentsRepository;
import com.blog.domain.posts.Post;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.CommentNotFound;
import com.blog.exception.PasswordNotMatches;
import com.blog.exception.PostNotFound;
import com.blog.web.dto.comments.CommentPassword;
import com.blog.web.dto.comments.CommentsResponse;
import com.blog.web.form.CommentEditForm;
import com.blog.web.form.CommentForm;
import com.blog.web.form.CommentPasswordCheckForm;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
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

        Comment comment = convertFormToCommentEntity(postId, form);
        commentsRepository.save(comment);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "commentCached",key = "{#id}")
    public CommentsResponse findById(Long id) {
        Comment comment = commentsRepository.findById(id).orElseThrow(CommentNotFound::new);
        return new CommentsResponse(comment);
    }

    public CommentsResponse checkAuthenticationAndReturnDto(CommentPasswordCheckForm form) {
        Comment comment = isPasswordValidate(form.getPassword(), form.getCommentId());
        return new CommentsResponse(comment);
    }

    public CommentPassword findEncodedPassword(Long id) {
        Comment comment = commentsRepository.findById(id).orElseThrow(CommentNotFound::new);
        return new CommentPassword(comment.getPassword());
    }

    @Transactional(readOnly = true)
    public List<CommentsResponse> findByPostId(Long postId) {
        return commentsRepository.findByPostId(postId).stream().map(CommentsResponse::new).toList();
    }

    @Transactional
    public void delete(CommentPasswordCheckForm form) {
        Comment comment = isPasswordValidate(form.getPassword(), form.getCommentId());
        commentsRepository.delete(comment);
    }

    @Transactional
    public void adminDelete(Long commentId) {
        Comment comment = commentsRepository.findById(commentId).orElseThrow(CommentNotFound::new);
        commentsRepository.delete(comment);
    }

    @Transactional
    public void edit(CommentEditForm form) {
        Comment comment = isPasswordValidate(form.getPassword(), form.getId());
        comment.edit(form.getContent());
    }

    //Method
    private Comment convertFormToCommentEntity(Long postId, CommentForm form) {
        Post post = postsRepository.findById(postId).orElseThrow(PostNotFound::new);
        Comment comment = commentsRepository.findById(form.getParentId()).orElse(null);
        return Comment.builder()
            .username(form.getUsername())
            .content(form.getContent())
            .parent(comment)
            .post(post)
            .password(passwordEncoder.encode(form.getPassword()))
            .build();
    }

    private Comment isPasswordValidate(String rawPassword, Long commentId) {
        Comment comment = commentsRepository.findById(commentId).orElseThrow(CommentNotFound::new);

        if (!passwordEncoder.matches(rawPassword, comment.getPassword())) {
            throw new PasswordNotMatches();
        }

        return comment;
    }
}
