package com.blog.web.controller;

import com.blog.service.CategoryService;
import com.blog.service.CommentService;
import com.blog.service.PostsService;
import com.blog.web.dto.category.CategoryResponse;
import com.blog.web.dto.posts.PostSaveRequest;
import com.blog.web.dto.posts.PostsResponse;
import com.blog.web.dto.posts.PostsUpdateRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("permitAll()")
    @GetMapping("/post/{postId}")
    public String getPostDetail(@PathVariable Long postId, Model model) {
        postsService.addViews(postId);
        PostsResponse postsResponse = postsService.getPostsByIdIncludingComments(postId);
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
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/post/new")
    public String createPostsForm(Model model) {
        addPostCreateForm(model);
        populateRelatedSidebar(model);
        return "form/createPostsForm";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/post/new")
    public String savePost(@Valid PostCreateForm form) {
        PostSaveRequest saveRequest = createPostSaveRequest(form);
        postsService.save(saveRequest);
        return "redirect:/";
    }

    // Edit
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/post/edit/{postId}")
    public String createEditForm(@PathVariable Long postId, Model model) {
        PostEditForm postEditForm = createEditPostForm(postId);
        model.addAttribute("editPostsForm", postEditForm);
        populateRelatedSidebar(model);
        return "form/editPostForm";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/post/edit/{postId}")
    public String editPost(@PathVariable Long postId, @Valid PostEditForm form) {
        PostsUpdateRequest request = createPostEditRequest(form);
        postsService.edit(postId, request);
        return "redirect:/post/{postId}";
    }

    private PostsUpdateRequest createPostEditRequest(PostEditForm form) {
        CategoryResponse categoryResponse = categoryService.findCategoryByTitle(form.getCategoryTitle());
        String thumbnail = postsService.getThumbnail(form.getContent());
        return PostsUpdateRequest.builder()
            .title(form.getTitle())
            .content(form.getContent())
            .tag(form.getTags())
            .category(categoryResponse.toEntity())
            .thumbnail(thumbnail)
            .build();
    }

    // Delete
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable Long postId) {
        postsService.delete(postId);
        return "redirect:/";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/post/search")
    public String getPostsClassifiedByKeyword(Pageable pageable, Model model
        , @RequestParam String q) {
        Page<PostsResponse> searchedPostsByKeyword = postsService.getPostsByKeyword(
            pageable, q);
        model.addAttribute("postsList", searchedPostsByKeyword);
        addKeywordAttributes(q,model);
        populateRelatedSidebar(model);
        return "index";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/post/category")
    public String getPostsClassifiedByCategory(Pageable pageable, @RequestParam String q,
        Model model) {
        Page<PostsResponse> categorizedPosts = postsService.getPostsSortedByCategory(
            pageable, q);
        model.addAttribute("categorizedPosts", categorizedPosts);
        addKeywordAttributes(q,model);
        populateRelatedSidebar(model);
        return "postsSortedByCategory";
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/tag")
    public String getPostsClassifiedByTags(Pageable pageable
        , @RequestParam String q, Model model) {
        Page<PostsResponse> postsByTags = postsService.getPostsByTag(pageable, q);
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
        List<CategoryResponse> allCategory = categoryService.findAllCategory();
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
        var anotherCategory = postsService.getPostsSortedByCategory(
            categoryTitle);
        model.addAttribute("anotherCategory",anotherCategory);
    }

    private void addCommentCount(Long postId, Model model) {
        int totalCommentSize = commentService.findByPostId(postId).size();
        model.addAttribute("commentSize", totalCommentSize);
    }

    private void addTags(Long postId, Model model) {
        List<String> tagList = postsService.getTags(postId);
        model.addAttribute("tagList", tagList);
    }

    private void addKeywordAttributes(String q,Model model) {
        model.addAttribute("keyword", q);
    }

    public PostSaveRequest createPostSaveRequest(PostCreateForm form) {
        CategoryResponse categoryResponse = categoryService.findCategoryByTitle(
            form.getCategoryTitle());
        String thumbnail = postsService.getThumbnail(form.getContent());
        return
            PostSaveRequest.builder()
                .title(form.getTitle())
                .content(form.getContent())
                .category(categoryResponse.toEntity())
                .tags(form.getTags())
                .views(0L)
                .thumbnail(thumbnail)
                .build();
    }

    public void addCommentPasswordCheckForm(Model model) {
        model.addAttribute("commentPasswordCheckForm", new CommentPasswordCheckForm());
    }

    public void addCommentForm(Model model) {
        model.addAttribute("commentForm", new CommentForm());
    }

    public PostEditForm createEditPostForm(Long postId) {

        //comment 안 불러와도 될 듯
        PostsResponse originalPost = postsService.getPostsByIdIncludingComments(postId);
        String title=originalPost.getTitle();
        String content=originalPost.getContent();
        String categoryTitle=originalPost.getCategoryTitle();
        String tags=originalPost.getTag();

        return new PostEditForm(title, content, categoryTitle,tags);
    }

    public void addPostCreateForm(Model model) {
        model.addAttribute("createPostsForm", new PostCreateForm());
    }
}
