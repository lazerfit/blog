package com.blog.domain.posts;

import com.blog.web.dto.PostsSearchRequestDto;
import java.util.List;

public interface PostsRepositoryCustom {
    List<Posts> getList(PostsSearchRequestDto request);
}
