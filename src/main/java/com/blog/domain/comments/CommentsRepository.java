package com.blog.domain.comments;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentsRepository extends JpaRepository<Comment,Long> {

    Comment findByPostsId(Long postsId);
}
