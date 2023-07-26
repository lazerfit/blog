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
    public Page<PostsResponse> fetchPostsExcludingComment(Pageable pageable) {
        return postsRepository.fetchPostsExcludingComment(pageable);
    }

    @Transactional
    public void delete(Long postId) {
        PostsResponse post = postsRepository.findPostById(postId).orElseThrow(PostNotFound::new);
        postsRepository.deleteById(post.getId());
    }

    public Page<PostsResponse> findPostsByKeyword(Pageable pageable,String q) {
        return postsRepository.findPostsByKeyword(pageable, q);
    }

    // Sorted By CategoryTitle
    public Page<PostsResponse> findPostsSortedByCategory(Pageable pageable,
        String categoryTitle) {
        return postsRepository.findPostsSortedByCategory(pageable, categoryTitle);
    }

    public List<PostsResponse> findPostsSortedByCategory(String categoryTitle) {
        return postsRepository.findPostsSortedByCategory(
            categoryTitle);
    }

    public List<String> fetchTags(Long id) {
        Post post = postsRepository.findById(id).orElseThrow(PostNotFound::new);
        String tags = post.getTag();
        return Stream.of(tags.split(",", -1)).toList();
    }

    public Page<PostsResponse> findPostsByTag(Pageable pageable, String tagTitle) {
        return postsRepository.findPostsByTag(pageable,tagTitle);
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
    public PostsResponse findPostsByIdIncludingComments(Long id) {
        return postsRepository.findPostsByIdIncludingComments(id);
    }
}


