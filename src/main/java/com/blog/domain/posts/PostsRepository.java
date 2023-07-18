package com.blog.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Post,Long>, PostsRepositoryCustom {

}
