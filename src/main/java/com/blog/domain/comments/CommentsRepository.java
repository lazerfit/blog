package com.blog.domain.comments;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comment,Long> {
    List<Comment> findByPostId(Long postId);
}
