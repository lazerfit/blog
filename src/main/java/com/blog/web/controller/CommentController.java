package com.blog.web.controller;

import com.blog.service.CommentService;
import com.blog.service.PostsService;
import com.blog.web.dto.CommentEditRequest;
import com.blog.web.dto.CommentPassword;
import com.blog.web.dto.CommentsResponseDto;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.form.CommentEditForm;
import com.blog.web.form.CommentForm;
import com.blog.web.form.CommentPasswordCheckForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    private final PostsService postsService;

    private final PasswordEncoder passwordEncoder;

    private static final String REPLACE_COMMENT_LIST = "posts :: commentList";

    @PostMapping("/posts/comment/new")
    public String insertComment(@RequestBody @Valid CommentForm form,
        Model model) {

        commentService.save(form.getPostId(), form);
        PostsResponseDto response = postsService.getPostsById(form.getPostId());
        model.addAttribute("postFindById", response);

        return REPLACE_COMMENT_LIST;
    }

    @PostMapping("/posts/admin/comment/delete")
    public String delete(@RequestParam Long commentId, @RequestParam Long postId, Model model) {
        commentService.delete(commentId);
        PostsResponseDto response = postsService.getPostsById(postId);
        model.addAttribute("postFindById", response);

        return REPLACE_COMMENT_LIST;
    }

    @PostMapping("/posts/comment/manage")
    public String test(CommentPasswordCheckForm form,Model model) {
        model.addAttribute("commentEdit",new CommentEditForm());
        CommentPassword password = commentService.findPassword(form.getCommentId());

        if (passwordEncoder.matches(form.getPassword(), password.password())) {
            CommentsResponseDto responseDto = commentService.findById(form.getCommentId());
            model.addAttribute("comment", responseDto);
            return "editComment";
        }
        return "/error/403";
    }

    @PostMapping("/posts/comment/manage/delete")
    @ResponseBody
    public String userDelete(CommentPasswordCheckForm form) {
        CommentPassword password = commentService.findPassword(form.getCommentId());

        if (passwordEncoder.matches(form.getPassword(), password.password())) {
            commentService.delete(form.getCommentId());
            return "성공";
        }
        return "비밀번호를 확인해주세요";
    }

    @PostMapping("/posts/comment/manage/edit")
    @ResponseBody
    public String userEdit(CommentEditForm form,Model model){

        CommentPassword password = commentService.findPassword(form.getId());

        if (passwordEncoder.matches(form.getPassword(), password.password())) {
            CommentEditRequest response = new CommentEditRequest(form.getId(),form.getContent());
            commentService.edit(response);
            return "성공";
        }
        return "비밀번호를 확인해주세요";
    }

    @ModelAttribute
    void pwd(Model model) {
        model.addAttribute("passwordForm", new CommentPasswordCheckForm());
    }
}
