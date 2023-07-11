package com.blog.domain.posts;

import com.blog.web.dto.PostsResponseDto;
import com.blog.web.dto.PostsResponseWithCategoryDto;
import com.blog.web.dto.PostsUpdateRequestDto;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostsRepositoryCustom {
    Page<PostsResponseDto> getPostsList(Pageable pageable);
    Page<PostsResponseDto> getPostsListByKeyword(Pageable pageable,String keyword);
    PostsResponseWithCategoryDto findByIdContainCategory(Long id);
    Page<PostsResponseWithCategoryDto> getCategorizedPosts(Pageable pageable,String category);
    Page<PostsResponseWithCategoryDto> getPostsByTags(Pageable pageable,String tag);
    List<Posts> getPopularPosts();

    List<PostsResponseDto> getCategorizedPostsNotContainPage(String q);
    void edit(Long id, PostsUpdateRequestDto requestDto);

    // Using When go into Posts Detail page
    PostsResponseDto findByIdWithQdsl(Long postId);
}
