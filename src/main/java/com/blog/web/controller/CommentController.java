package com.blog.web.controller;

import com.blog.service.CommentService;
import com.blog.service.PostsService;
import com.blog.web.dto.comments.CommentsResponse;
import com.blog.web.dto.posts.PostsResponse;
import com.blog.web.form.CommentEditForm;
import com.blog.web.form.CommentForm;
import com.blog.web.form.CommentPasswordCheckForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private static final String SUCCESS = "성공";
    private static final String CHECK_PASSWORD = "비밀번호를 확인해 주세요";

    @PreAuthorize("permitAll()")
    @PostMapping("/post/comment/new")
    public String saveComment(@RequestBody @Valid CommentForm form,
        Model model) {
        addCommentPasswordCheckForm(model);
        commentService.save(form.getPostId(), form);
        addAsynchronousAttributes(form.getPostId(), model);
        return REPLACE_COMMENT_LIST;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/post/admin/comment/delete")
    public String delete(@RequestParam Long commentId, @RequestParam Long postId, Model model) {
        addCommentPasswordCheckForm(model);
        commentService.adminDelete(commentId);
        addAsynchronousAttributes(postId, model);
        return REPLACE_COMMENT_LIST;
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/post/comment/manage")
    public String manageComment(@Valid CommentPasswordCheckForm form, Model model) {
        addCommentPasswordCheckForm(model);
        CommentsResponse response = commentService.checkAuthenticationAndReturnDto(form);
        model.addAttribute("comment", response);
        addCommentEditForm(model);
        return "editComment";
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/post/comment/manage/delete")
    @ResponseBody
    public void userDeleteComment(@RequestBody @Valid CommentPasswordCheckForm form) {
        commentService.delete(form);
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/post/comment/manage/edit")
    @ResponseBody
    public void userEditComment(@Valid CommentEditForm form,Model model){
        addCommentPasswordCheckForm(model);
        commentService.edit(form);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/post/comment/subComment/new")
    public String subComment(@RequestParam Long commentId,@RequestParam Long postId ,Model model) {
        model.addAttribute("parentId", commentId);
        model.addAttribute("postId", postId);
        return "subComment";
    }

    @PreAuthorize("permitAll()")
    @PostMapping("/post/comment/subComment/new")
    @ResponseBody
    public String subComment(@RequestBody @Valid CommentForm form) {
        commentService.save(form.getPostId(),form);
        return SUCCESS;
    }


    //Method
    private void addAsynchronousAttributes(Long postId, Model model) {
        PostsResponse response = postsService.getPostsByIdIncludingComments(postId);
        model.addAttribute("postFindById", response);
    }

    public void addCommentPasswordCheckForm(Model model) {
        model.addAttribute("commentPasswordCheckForm", new CommentPasswordCheckForm());
    }

    public void addCommentEditForm(Model model) {
        model.addAttribute("commentEdit", new CommentEditForm());
    }
}
