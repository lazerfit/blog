package com.blog.web.controller;

import com.blog.service.CommentService;
import com.blog.service.PostsService;
import com.blog.web.dto.CommentEditRequest;
import com.blog.web.dto.CommentPassword;
import com.blog.web.dto.CommentsResponseDto;
import com.blog.web.dto.PostsResponse;
import com.blog.web.form.CommentEditForm;
import com.blog.web.form.CommentForm;
import com.blog.web.form.CommentPasswordCheckForm;
import com.blog.web.form.FormHandler;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final FormHandler formHandler;

    private static final String REPLACE_COMMENT_LIST = "posts :: commentList";
    private static final String SUCCESS = "성공";
    private static final String CHECK_PASSWORD = "비밀번호를 확인해 주세요";

    @PostMapping("/post/comment/new")
    public String saveComment(@RequestBody @Valid CommentForm form,
        Model model) {

        formHandler.addCommentPasswordCheckForm(model);

        commentService.save(form.getPostId(), form);

        PostsResponse response = postsService.findPostsByIdIncludingComments(form.getPostId());
        model.addAttribute("postFindById", response);

        return REPLACE_COMMENT_LIST;
    }

    @PostMapping("/post/admin/comment/delete")
    public String delete(@RequestParam Long commentId, @RequestParam Long postId, Model model) {
        commentService.delete(commentId);
        PostsResponse response = postsService.findPostsByIdIncludingComments(postId);
        model.addAttribute("postFindById", response);

        return REPLACE_COMMENT_LIST;
    }

    @PostMapping("/post/comment/manage")
    public String manageComment(CommentPasswordCheckForm form, Model model) {

        formHandler.addCommentPasswordCheckForm(model);

        if (isPasswordValid(form)) {

            CommentsResponseDto responseDto = findCommentById(form.getCommentId());

            model.addAttribute("comment", responseDto);
            formHandler.addCommentEditForm(model);

            return "editComment";
        }
        return "/error/403";
    }

    @PostMapping("/post/comment/manage/delete")
    @ResponseBody
    public String userDeleteComment(CommentPasswordCheckForm form) {

        if (isPasswordValid(form)) {
            commentService.delete(form.getCommentId());
            return SUCCESS;
        }
        return CHECK_PASSWORD;
    }

    @PostMapping("/post/comment/manage/edit")
    @ResponseBody
    public String userEditComment(CommentEditForm form,Model model){

        formHandler.addCommentPasswordCheckForm(model);

        if (isPasswordValid(form)) {
            CommentEditRequest response = new CommentEditRequest(form.getId(),form.getContent());
            commentService.edit(response);
            return SUCCESS;
        }
        return CHECK_PASSWORD;
    }

    @GetMapping("/post/comment/subComment/new")
    public String subComment(@RequestParam Long commentId,@RequestParam Long postId ,Model model) {

        model.addAttribute("parentId", commentId);
        model.addAttribute("postId", postId);

        return "subComment";
    }

    @PostMapping("/post/comment/subComment/new")
    @ResponseBody
    public String subComment(@RequestBody CommentForm form) {

        commentService.save(form.getPostId(),form);

        return SUCCESS;
    }


    //Method
    private CommentsResponseDto findCommentById(Long commentId) {
        return commentService.findById(commentId);
    }

    private boolean isPasswordValid(CommentEditForm form) {
        CommentPassword encodedPassword = findEncodedPassword(form.getId());
        return passwordEncoder.matches(form.getPassword(), encodedPassword.password());
    }

    private boolean isPasswordValid(CommentPasswordCheckForm form) {
        CommentPassword encodedPassword = findEncodedPassword(form.getCommentId());
        return passwordEncoder.matches(form.getPassword(), encodedPassword.password());
    }

    private CommentPassword findEncodedPassword(Long commentId) {
        return commentService.findPassword(commentId);
    }
}
