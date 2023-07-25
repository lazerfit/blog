package com.blog.web.controller;

import com.blog.domain.category.Category;
import com.blog.service.CategoryService;
import com.blog.service.CommentService;
import com.blog.service.PostsService;
import com.blog.web.dto.posts.PostSaveRequest;
import com.blog.web.dto.posts.PostsResponse;
import com.blog.web.dto.posts.PostsUpdateRequest;
import com.blog.web.form.CommentEditForm;
import com.blog.web.form.CommentForm;
import com.blog.web.form.CommentPasswordCheckForm;
import com.blog.web.form.PostCreateForm;
import com.blog.web.form.PostEditForm;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostsController {

    private final PostsService postsService;
    private final CategoryService categoryService;
    private final CommentService commentService;

    @GetMapping("/")
    public String index(Pageable pageable, Model model) {
        Page<PostsResponse> posts = postsService.fetchPostsExcludingComment(pageable);
        model.addAttribute("postsList", posts);
        populateRelatedSidebar(model);
        return "index";
    }

    @GetMapping("/post/{postId}")
    public String getPostDetail(@PathVariable Long postId, Model model) {
        postsService.addViews(postId);
        PostsResponse postsResponse = postsService.findPostsByIdIncludingComments(postId);
        model.addAttribute("postFindById", postsResponse);
        // Add attributes about category, popular post
        populateRelatedSidebar(model);
        // Add details about related categories, comments, tags
        populateRelatedDetails(postId, model, postsResponse);
        addCommentForm(model);
        addCommentPasswordCheckForm(model);
        return "posts";
    }

    // Create
    @GetMapping("/post/new")
    public String createPostsForm(Model model) {
        addPostCreateForm(model);
        populateRelatedSidebar(model);
        return "form/createPostsForm";
    }

    @PostMapping("/post/new")
    public String savePost(@Valid PostCreateForm form) {
        PostSaveRequest saveRequest = createPostSaveRequest(form);
        postsService.save(saveRequest);
        return "redirect:/";
    }

    // Edit
    @GetMapping("/post/edit/{postId}")
    public String createEditForm(@PathVariable Long postId, Model model) {
        PostEditForm postEditForm = createEditPostForm(postId);
        model.addAttribute("editPostsForm", postEditForm);
        populateRelatedSidebar(model);
        return "form/editPostForm";
    }

    @PostMapping("/post/edit/{postId}")
    public String editPost(@PathVariable Long postId, @RequestBody @Valid PostEditForm form) {
        PostsUpdateRequest request = createPostEditRequest(form);
        postsService.edit(postId, request);
        return "redirect:/post/{postId}";
    }

    private PostsUpdateRequest createPostEditRequest(PostEditForm form) {
        Category category = categoryService.findCategoryByTitle(form.getCategoryTitle());
        return PostsUpdateRequest.builder()
            .title(form.getTitle())
            .content(form.getContent())
            .tag(form.getTag())
            .category(category)
            .build();
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
        Page<PostsResponse> categorizedPosts = postsService.fetchPostsSortedByCategory(
            pageable, q);
        model.addAttribute("categorizedPosts", categorizedPosts);
        addKeywordAttributes(q,model);
        populateRelatedSidebar(model);
        return "postsSortedByCategory";
    }

    @GetMapping("/tag")
    public String getPostsClassifiedByTags(Pageable pageable
        , @RequestParam String q, Model model) {
        Page<PostsResponse> postsByTags = postsService.findPostsByTag(pageable, q);
        model.addAttribute("postsByTags", postsByTags);
        addKeywordAttributes(q,model);
        return "postsSortedByTags";
    }

    // Method
    private void populateRelatedSidebar(Model model) {
        addCategoriesAttributes(model);
        addPopularPostsAttributes(model);
    }

    private void addCategoriesAttributes(Model model) {
        List<Category> allCategory = categoryService.findAllCategory();
        model.addAttribute("allCategorizedPosts", allCategory);
    }

    private void addPopularPostsAttributes(Model model) {
        List<PostsResponse> popularPosts = postsService.getPopularPosts();
        model.addAttribute("popularPosts",popularPosts);
    }

    private void populateRelatedDetails(Long postId, Model model, PostsResponse postsResponse) {
        addAnotherCategories(postsResponse.getCategoryTitle(), model);
        addCommentCount(postId, model);
        addTags(postId, model);
    }

    private void addAnotherCategories(String categoryTitle,Model model) {
        var anotherCategory = postsService.fetchPostsSortedByCategory(
            categoryTitle);
        model.addAttribute("anotherCategory",anotherCategory);
    }

    private void addCommentCount(Long postId, Model model) {
        int totalCommentSize = commentService.findByPostId(postId).size();
        model.addAttribute("commentSize", totalCommentSize);
    }

    private void addTags(Long postId, Model model) {
        List<String> tagList = postsService.fetchTags(postId);
        model.addAttribute("tagList", tagList);
    }

    private void addKeywordAttributes(String q,Model model) {
        model.addAttribute("keyword", q);
    }

    public PostSaveRequest createPostSaveRequest(PostCreateForm form) {
        Category category = categoryService.findCategoryByTitle(form.getCategoryTitle());
        return new PostSaveRequest(
            form.getTitle(),form.getContent(),category,form.getTags(),0L);
    }

    public void addCommentPasswordCheckForm(Model model) {
        model.addAttribute("commentPasswordCheckForm", new CommentPasswordCheckForm());
    }

    public void addCommentForm(Model model) {
        model.addAttribute("commentForm", new CommentForm());
    }

    public PostEditForm createEditPostForm(Long postId) {

        PostsResponse originalPost = postsService.findPostsByIdIncludingComments(postId);
        String title=originalPost.getTitle();
        String content=originalPost.getContent();
        String categoryTitle=originalPost.getCategoryTitle();
        String tag=originalPost.getTag();

        return new PostEditForm(title, content, categoryTitle,tag);
    }

    public void addPostCreateForm(Model model) {
        model.addAttribute("createPostsForm", new PostCreateForm());
    }

    public void addCommentEditForm(Model model) {
        model.addAttribute("commentEdit", new CommentEditForm());
    }

}
