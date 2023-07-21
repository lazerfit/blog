package com.blog.web.controller;

import com.blog.domain.category.Category;
import com.blog.service.CategoryService;
import com.blog.service.CommentService;
import com.blog.service.PostsService;
import com.blog.web.dto.PostSaveRequest;
import com.blog.web.dto.PostsResponse;
import com.blog.web.dto.PostsResponseWithCategoryDto;
import com.blog.web.dto.PostsResponseWithoutComment;
import com.blog.web.dto.PostsUpdateRequestDto;
import com.blog.web.form.PostCreateForm;
import com.blog.web.form.PostEditForm;
import com.blog.web.form.FormHandler;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostsController {

    private final PostsService postsService;

    private final CategoryService categoryService;

    private final CommentService commentService;

    private final FormHandler formHandler;

    @GetMapping("/")
    public String index(Pageable pageable, Model model) {

        Page<PostsResponseWithoutComment> posts = postsService.getPostsExcludingComment(pageable);
        model.addAttribute("postsList", posts);

        populateRelatedSidebar(model);

        return "index";
    }


    @GetMapping("/post/{postId}")
    public String getPostDetail(@PathVariable Long postId, Model model) {

        postsService.addViews(postId);

        PostsResponse postsResponse = postsService.findPostsByIdIncludingComments(postId);
        model.addAttribute("postFindById", postsResponse);

        formHandler.addCommentForm(model);
        formHandler.addCommentPasswordCheckForm(model);

        // Add attributes about category, popular post
        populateRelatedSidebar(model);

        // Add details about related categories, comments, tags
        populateRelatedDetails(postId, model, postsResponse);

        return "posts";
    }

    // Create
    @GetMapping("/post/new")
    public String createPostsForm(Model model) {

        formHandler.addPostCreateForm(model);

        populateRelatedSidebar(model);

        return "form/createPostsForm";
    }

    @PostMapping("/post/new")
    public String savePost(@Valid PostCreateForm form) {

        PostSaveRequest saveRequest = formHandler.createSaveRequest(form);

        postsService.save(saveRequest);

        return "redirect:/";
    }

    // Edit
    @GetMapping("/post/edit/{postId}")
    public String createEditForm(@PathVariable Long postId, Model model) {

        PostEditForm postEditForm = formHandler.createEditPostForm(postId);

        model.addAttribute("editPostsForm", postEditForm);

        populateRelatedSidebar(model);

        return "form/editPostsForm";
    }

    @PostMapping("/post/edit/{postId}")
    public String editPost(@PathVariable Long postId, @Valid PostEditForm form) {

        // Consider refactoring the logic for dirty checking

        Category categoryByTitle = categoryService.getCategoryByTitle(form.getCategoryTitle());

        PostsUpdateRequestDto request = new PostsUpdateRequestDto(form.getTitle(),
            form.getContent(), categoryByTitle);

        postsService.edit(postId, request);

        return "redirect:/post/{postId}";
    }


    // Delete
    @PostMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {

        postsService.delete(postId);

        return "redirect:/";
    }

    @GetMapping("/post/search")
    public String getPostsClassifiedByKeyword(Pageable pageable, Model model
        , @RequestParam String q) {

        Page<PostsResponse> searchedPostsByKeyword = postsService.findPostsByKeyword(
            pageable, q);
        model.addAttribute("postsList", searchedPostsByKeyword);

        addKeywordAttributes(q,model);
        populateRelatedSidebar(model);

        return "index";
    }

    @GetMapping("/post/category")
    public String getPostsClassifiedByCategory(Pageable pageable, @RequestParam String q,
        Model model) {

        Page<PostsResponseWithCategoryDto> categorizedPosts = postsService.getCategorizedPosts(
            pageable, q);
        model.addAttribute("categorizedPosts", categorizedPosts);

        addKeywordAttributes(q,model);

        return "categorizedPosts";
    }

    @GetMapping("/tag")
    public String getPostsClassifiedByTags(Pageable pageable
        , @RequestParam String q, Model model) {

        Page<PostsResponseWithCategoryDto> postsByTags = postsService.getPostsByTags(pageable, q);
        model.addAttribute("postsByTags", postsByTags);

        addKeywordAttributes(q,model);

        return "postsByTags";
    }


    // Method
    private void addCategoriesAttributes(Model model) {

        List<Category> allCategory = categoryService.findAllCategory();
        model.addAttribute("allCategorizedPosts", allCategory);
    }

    private void addPopularPostsAttributes(Model model) {
        List<PostsResponseWithoutComment> popularPosts = postsService.getPopularPosts();
        model.addAttribute("popularPosts",popularPosts);
    }

    private void addKeywordAttributes(String q,Model model) {
        model.addAttribute("keyword", q);
    }

    private void addAnotherCategories(PostsResponse postsResponse,Model model) {
        var anotherCategory = postsService.getCategorizedPostsNotContainPage(
            postsResponse.getCategoryTitle());
        model.addAttribute("anotherCategory",anotherCategory);
    }

    private void addTags(Long postId, Model model) {
        List<String> tagList = postsService.getTags(postId);
        model.addAttribute("tagList", tagList);
    }

    private void addCommentCount(Long postId, Model model) {
        int totalCommentSize = commentService.findByPostsId(postId).size();
        model.addAttribute("commentSize", totalCommentSize);
    }

    private void populateRelatedSidebar(Model model) {
        addCategoriesAttributes(model);
        addPopularPostsAttributes(model);
    }

    private void populateRelatedDetails(Long postId, Model model, PostsResponse postsResponse) {
        addAnotherCategories(postsResponse, model);
        addCommentCount(postId, model);
        addTags(postId, model);
    }
}
