package com.blog.web.form;

import com.blog.domain.category.Category;
import com.blog.service.CategoryService;
import com.blog.service.PostsService;
import com.blog.web.dto.PostSaveRequest;
import com.blog.web.dto.PostsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
@RequiredArgsConstructor
public class FormHandler {

    private final CategoryService categoryService;
    private final PostsService postsService;

    public void addCommentForm(Model model) {
        model.addAttribute("commentForm", new CommentForm());
    }

    public void addCommentPasswordCheckForm(Model model) {
        model.addAttribute("passwordForm", new CommentPasswordCheckForm());
    }

    public PostSaveRequest createSaveRequest(PostCreateForm form) {

        Category category = categoryService.getCategoryByTitle(form.getCategoryTitle());

        return new PostSaveRequest(
            form.getTitle(),form.getContent(),category,form.getTags(),0L);
    }

    public PostEditForm createEditPostForm(Long postId) {

        PostsResponse originalPost = postsService.findPostsByIdIncludingComments(postId);
        String title=originalPost.getTitle();
        String content=originalPost.getContent();
        String categoryTitle=originalPost.getCategoryTitle();

        return new PostEditForm(title, content, categoryTitle);
    }

    public void addPostCreateForm(Model model) {
        model.addAttribute("createPostsForm", new PostCreateForm());
    }

    public void addCommentEditForm(Model model) {
        model.addAttribute("commentEdit", new CommentEditForm());
    }

}
