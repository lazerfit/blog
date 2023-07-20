package com.blog.web.controller;

import com.blog.domain.category.Category;
import com.blog.service.CategoryService;
import com.blog.service.CommentService;
import com.blog.service.PostsService;
import com.blog.web.dto.PostsResponse;
import com.blog.web.dto.PostsResponseWithCategoryDto;
import com.blog.web.dto.PostsResponseWithoutCommentDto;
import com.blog.web.dto.PostsSaveRequestDto;
import com.blog.web.dto.PostsUpdateRequestDto;
import com.blog.web.form.CommentForm;
import com.blog.web.form.CommentPasswordCheckForm;
import com.blog.web.form.CreatePostsForm;
import com.blog.web.form.EditPostsForm;
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

    @GetMapping("/")
    public String index(Pageable pageable, Model model) {
        Page<PostsResponseWithoutCommentDto> posts = postsService.getPostsWithPaging(pageable);
        model.addAttribute("postsList", posts);

        addCategoriesAttributes(model);
        addPopularPostsAttributes(model);

        return "index";
    }


    @GetMapping("/post/{postId}")
    public String getPostDetail(@PathVariable Long postId, Model model) {

        postsService.addViews(postId);

        PostsResponse postsResponse = postsService.getPostsByIdWithComments(postId);
        model.addAttribute("postFindById", postsResponse);

        addCommentFormAndPasswordForm(model);

        // 카테고리의 다른 글
        addAnotherCategories(postsResponse,model);

        addCommentCount(postId, model);
        addTags(postId, model);
        addCategoriesAttributes(model);
        addPopularPostsAttributes(model);
        return "posts";
    }

    // Create
    @GetMapping("/post/new")
    public String createPostsForm(Model model) {

        model.addAttribute("createPostsForm", new CreatePostsForm());

        addCategoriesAttributes(model);
        addPopularPostsAttributes(model);

        return "form/createPostsForm";
    }

    @PostMapping("/post/new")
    public String savePost(@Valid CreatePostsForm form) {

        PostsSaveRequestDto saveRequest = createSaveRequest(form);

        postsService.save(saveRequest);

        return "redirect:/";
    }

    // Edit
    @GetMapping("/post/edit/{postId}")
    public String createEditForm(@PathVariable Long postId, Model model) {

        EditPostsForm editPostsForm = createEditPostsForm(postId);

        model.addAttribute("editPostsForm", editPostsForm);

        addCategoriesAttributes(model);
        addPopularPostsAttributes(model);

        return "form/editPostsForm";
    }

    @PostMapping("/post/edit/{postId}")
    public String editPost(@PathVariable Long postId, @Valid EditPostsForm form) {

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

        Page<PostsResponse> searchedPostsListByKeyword = postsService.getSearchedPostsListByKeyword(
            pageable, q);
        model.addAttribute("postsList", searchedPostsListByKeyword);

        addKeywordAttributes(q,model);
        addCategoriesAttributes(model);
        addPopularPostsAttributes(model);

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
        List<PostsResponseWithoutCommentDto> popularPosts = postsService.getPopularPosts();
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
        List<String> tagList = postsService.getTagsAsList(postId);
        model.addAttribute("tagList", tagList);
    }

    private void addCommentCount(Long postId, Model model) {
        int totalCommentSize = commentService.findByPostsId(postId).size();
        model.addAttribute("commentSize", totalCommentSize);
    }

    private void addCommentFormAndPasswordForm(Model model) {
        model.addAttribute("commentForm", new CommentForm());
        model.addAttribute("passwordForm", new CommentPasswordCheckForm());
    }

    private EditPostsForm createEditPostsForm(Long postId) {

        PostsResponse originalPost = postsService.getPostsByIdWithComments(postId);
        String title=originalPost.getTitle();
        String content=originalPost.getContent();
        String categoryTitle=originalPost.getCategoryTitle();

        return new EditPostsForm(title, content, categoryTitle);
    }

    private PostsSaveRequestDto createSaveRequest(CreatePostsForm form) {

        Category category = categoryService.getCategoryByTitle(form.getCategoryTitle());

        return new PostsSaveRequestDto(
            form.getTitle(),form.getContent(),category,form.getTags(),0L);
    }
}
