package com.blog.web.controller;

import com.blog.domain.category.Category;
import com.blog.service.CategoryService;
import com.blog.service.PostsService;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.dto.PostsResponseWithCategoryDto;
import com.blog.web.dto.PostsSaveRequestDto;
import com.blog.web.dto.PostsUpdateRequestDto;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@Controller
public class PostsController {

    private final PostsService postsService;

    private final CategoryService categoryService;

    @GetMapping("/posts/new")
    public String createPostsForm(Model model) {
        model.addAttribute("createPostsForm", new CreatePostsForm());
        return "form/createPostsForm";
    }

    @PostMapping("/posts/new")
    public String save(@Valid CreatePostsForm form) {
        Category getCategoryByTitle = categoryService.getCategoryByTitle(form.getCategoryTitle());
        PostsSaveRequestDto request = new PostsSaveRequestDto(form.getTitle(), form.getContent(),
            getCategoryByTitle, form.getTags(),0L);
        postsService.save(request);
        return "redirect:/";
    }

    @GetMapping("/posts/edit/{postId}")
    public String editForm(@PathVariable Long postId, Model model) {
        PostsResponseWithCategoryDto originalPosts = postsService.findByIdWithCategory(postId);
        EditPostsForm editPostsForm = new EditPostsForm(originalPosts.getTitle(),
            originalPosts.getContent(), originalPosts.getCategoryTitle());
        model.addAttribute("editPostsForm", editPostsForm);
        return "form/editPostsForm";
    }


    @PostMapping("/posts/edit/{postId}")
    public String edit(@PathVariable Long postId, @Valid EditPostsForm form) {
        Category categoryByTitle = categoryService.getCategoryByTitle(form.getCategoryTitle());
        PostsUpdateRequestDto request = new PostsUpdateRequestDto(form.getTitle(),
            form.getContent(), categoryByTitle);
        postsService.edit(postId, request);
        return "redirect:/posts/{postId}";
    }

    @GetMapping("/posts/{postId}")
    public String findById(@PathVariable Long postId, Model model) {
        postsService.addHit(postId);
        PostsResponseDto postsResponseDto = postsService.getPostsById(postId);
        List<String> tagList = postsService.getTagsAsList(postId);
        var anotherCategory = postsService.getCategorizedPostsNotContainPage(
            postsResponseDto.getCategory().getTitle());
        model.addAttribute("postFindById", postsResponseDto);
        model.addAttribute("tagList", tagList);
        model.addAttribute("anotherCategory",anotherCategory);
        return "posts";
    }

    @PostMapping("/posts/delete/{postId}")
    public String delete(@PathVariable Long postId) {
        postsService.delete(postId);
        return "redirect:/";
    }

    @GetMapping("/posts/search")
    public String searchPostsByKeyword(Pageable pageable, Model model
        , @RequestParam String q) {
        Page<PostsResponseDto> searchedPostsListByKeyword = postsService.getSearchedPostsListByKeyword(
            pageable, q);
        model.addAttribute("postsList", searchedPostsListByKeyword);
        return "index";
    }

    @GetMapping("/posts/category")
    public String getCategorizedPosts(Pageable pageable, @RequestParam String q,
        Model model) {
        Page<PostsResponseWithCategoryDto> categorizedPosts = postsService.getCategorizedPosts(
            pageable, q);
        model.addAttribute("categorizedPosts", categorizedPosts);

        return "categorizedPosts";
    }

    @GetMapping("/tag")
    public String getPostsClassifiedByTags(Pageable pageable
        , @RequestParam String q, Model model) {
        Page<PostsResponseWithCategoryDto> postsByTags = postsService.getPostsByTags(pageable, q);
        model.addAttribute("postsByTags", postsByTags);
        return "postsByTags";
    }

    @ModelAttribute
    public void commonLayoutAttribute(Model model, String q) {
        List<Category> allCategory = categoryService.findAllCategory();
        model.addAttribute("allCategorizedPosts", allCategory);
        List<PostsResponseDto> popularPosts = postsService.getPopularPosts();
        model.addAttribute("popularPosts",popularPosts);
        model.addAttribute("keyword", q);
    }
}
