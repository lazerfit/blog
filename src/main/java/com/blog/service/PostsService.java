package com.blog.service;

import com.blog.domain.posts.Post;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.PostNotFound;
import com.blog.web.dto.posts.PostSaveRequest;
import com.blog.web.dto.posts.PostsResponse;
import com.blog.web.dto.posts.PostsUpdateRequest;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public void save(PostSaveRequest request) {
        postsRepository.save(request.toEntity());
    }

    @Transactional
    public void edit(Long postId, PostsUpdateRequest request) {
        Post post = postsRepository.findById(postId).orElseThrow(PostNotFound::new);
        post.edit(request);
    }

    @Transactional(readOnly = true)
    public Page<PostsResponse> getPostsExcludingComment(Pageable pageable) {
        return postsRepository.fetchPostsExcludingComment(pageable);
    }

    @Transactional
    @CacheEvict(value = "postCached", key = "{#postId}")
    public void delete(Long postId) {
        PostsResponse post = postsRepository.findPostById(postId).orElseThrow(PostNotFound::new);
        postsRepository.deleteById(post.getId());
    }

    public Page<PostsResponse> getPostsByKeyword(Pageable pageable,String q) {
        return postsRepository.getPostsByKeyword(pageable, q);
    }

    // Sorted By CategoryTitle
    public Page<PostsResponse> getPostsSortedByCategory(Pageable pageable,
        String categoryTitle) {
        return postsRepository.getPostsSortedByCategory(pageable, categoryTitle);
    }

    public List<PostsResponse> getPostsSortedByCategory(String categoryTitle) {
        return postsRepository.getPostsSortedByCategory(
            categoryTitle);
    }

    public List<String> getTags(Long id) {
        Post post = postsRepository.findById(id).orElseThrow(PostNotFound::new);
        String tags = post.getTag();
        return Stream.of(tags.split(",", -1)).toList();
    }

    public Page<PostsResponse> getPostsByTag(Pageable pageable, String tagTitle) {
        return postsRepository.getPostsByTag(pageable,tagTitle);
    }

    @Transactional
    public void addViews(Long id) {
        Post post = postsRepository.findById(id).orElseThrow(PostNotFound::new);
        post.addViews(1L);
    }

    public List<PostsResponse> getPopularPosts() {
        return postsRepository.getPopularPosts();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "postCached",key = "{#id}" , unless = "#result==null")
    public PostsResponse getPostsByIdIncludingComments(Long id) {
        return postsRepository.getPostsByIdIncludingComments(id);
    }
}



