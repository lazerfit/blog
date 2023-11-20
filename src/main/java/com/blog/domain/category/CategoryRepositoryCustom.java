package com.blog.domain.category;

import com.blog.web.dto.posts.PostsResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryRepositoryCustom {
    // 삭제해도 될 듯
    Page<PostsResponse> getCategorizedPostsList(Pageable pageable,String q);


}
