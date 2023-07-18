package com.blog.domain.posts;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Post,Long>, PostsRepositoryCustom {

    Optional<Post> getPostById(Long postId);

}
