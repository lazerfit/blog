package com.blog.domain.posts;

import jakarta.persistence.LockModeType;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface PostsRepository extends JpaRepository<Post,Long>, PostsRepositoryCustom {

    void deleteById(@NotNull Long postId);

    @Lock(LockModeType.OPTIMISTIC)
    @Query("select p from Post p where p.id= :id")
    Optional<Post> findByIdWithOptimisticLock(Long id);
}
