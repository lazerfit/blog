package com.blog.domain.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostsRepositoryCustom {
    Page<Posts> getPostsList(Pageable pageable);
}
