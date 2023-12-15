package com.blog.service;

import com.blog.domain.posts.Post;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.PostNotFound;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
@Service
@RequiredArgsConstructor
public class OptimisticLockPostService {

    private final PostsRepository postsRepository;

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void addViews(Long id) {
        Post post = postsRepository.findByIdWithOptimisticLock(id).orElseThrow(PostNotFound::new);
        post.addViews(1L);
    }
}
