package com.blog.domain.comments;

import com.blog.web.dto.CommentsResponseDto;
import java.util.List;

public interface CommentRepositoryCustom {

    List<CommentsResponseDto> findByPostsId(Long postId);

}
