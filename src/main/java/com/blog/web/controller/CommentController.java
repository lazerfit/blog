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

    private static final String REPLACE_COMMENT_LIST = "posts :: commentList";

    @PostMapping("/post/comment/new")
    public String saveComment(@RequestBody @Valid CommentForm form,
        Model model) {

        addPasswordCheckForm(model);

        commentService.save(form.getPostId(), form);

        PostsResponse response = postsService.getPostsByIdWithComments(form.getPostId());
        model.addAttribute("postFindById", response);

        return REPLACE_COMMENT_LIST;
    }

    @PostMapping("/post/admin/comment/delete")
    public String delete(@RequestParam Long commentId, @RequestParam Long postId, Model model) {
        commentService.delete(commentId);
        PostsResponse response = postsService.getPostsByIdWithComments(postId);
        model.addAttribute("postFindById", response);

        return REPLACE_COMMENT_LIST;
    }

    @PostMapping("/post/comment/manage")
    public String test(CommentPasswordCheckForm form, Model model) {

        model.addAttribute("commentEdit", new CommentEditForm());

        addPasswordCheckForm(model);

        if (passwordCheck(form)) {
            CommentsResponseDto responseDto = commentService.findById(form.getCommentId());
            model.addAttribute("comment", responseDto);
            return "editComment";
        }
        return "/error/403";
    }

    private Boolean passwordCheck(CommentPasswordCheckForm form) {
        CommentPassword encodedPassword = getEncodedPassword(form);
        return passwordEncoder.matches(form.getPassword(), encodedPassword.password());
    }

    private CommentPassword getEncodedPassword(CommentPasswordCheckForm form) {
        return commentService.findPassword(form.getCommentId());
    }

    @PostMapping("/post/comment/manage/delete")
    @ResponseBody
    public String userDelete(CommentPasswordCheckForm form) {

        if (passwordCheck(form)) {
            commentService.delete(form.getCommentId());
            return "성공";
        }
        return "비밀번호를 확인해주세요";
    }

    @PostMapping("/post/comment/manage/edit")
    @ResponseBody
    public String userEdit(CommentEditForm form,Model model){

        addPasswordCheckForm(model);

        CommentPassword password = commentService.findPassword(form.getId());

        if (passwordEncoder.matches(form.getPassword(), password.password())) {
            CommentEditRequest response = new CommentEditRequest(form.getId(),form.getContent());
            commentService.edit(response);
            return "성공";
        }
        return "비밀번호를 확인해주세요";
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

        return "";
    }


    private void addPasswordCheckForm(Model model) {
        model.addAttribute("passwordForm", new CommentPasswordCheckForm());
    }
}
