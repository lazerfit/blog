package com.blog.domain.posts;

import com.blog.web.dto.PostsResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostsRepositoryCustom {
    Page<PostsResponseDto> getPostsList(Pageable pageable);
    Page<PostsResponseDto> getPostsListByKeyword(Pageable pageable,String keyword);
}
