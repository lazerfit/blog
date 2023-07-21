package com.blog.domain.posts;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostsRepository extends JpaRepository<Post,Long>, PostsRepositoryCustom {

    void deleteById(@NotNull Long postId);


}
