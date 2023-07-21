package com.blog.service;

import com.blog.domain.posts.Post;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.PostNotFound;
import com.blog.web.dto.PostsResponse;
import com.blog.web.dto.PostsResponseWithCategoryDto;
import com.blog.web.dto.PostsResponseWithoutComment;
import com.blog.web.dto.PostSaveRequest;
import com.blog.web.dto.PostsUpdateRequestDto;
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
    public void edit(Long id, PostsUpdateRequestDto request) {
        postsRepository.edit(id,request);
    }

    @Transactional(readOnly = true)
    public Page<PostsResponseWithoutComment> fetchPostsExcludingComment(Pageable pageable) {
        return postsRepository.fetchPostsExcludingComment(pageable);
    }

    @Transactional
    public void delete(Long postId) {
        PostsResponse post = postsRepository.findPostsById(postId).orElseThrow(PostNotFound::new);
        postsRepository.deleteById(post.getId());
    }

    public Page<PostsResponse> findPostsByKeyword(Pageable pageable,String q) {
        return postsRepository.findPostsByKeyword(pageable, q);
    }

    // Sorted By CategoryTitle
    public Page<PostsResponseWithCategoryDto> fetchPostsSortedByCategory(Pageable pageable,
        String categoryTitle) {
        return postsRepository.fetchPostsSortedByCategory(pageable, categoryTitle);
    }

    public List<PostsResponse> fetchPostsSortedByCategory(String categoryTitle) {
        return postsRepository.fetchPostsSortedByCategory(
            categoryTitle);
    }

    public List<String> fetchTags(Long id) {
        Post post = postsRepository.findById(id).orElseThrow(PostNotFound::new);
        String tags = post.getTag();
        return Stream.of(tags.split(",", -1)).toList();
    }

    public Page<PostsResponseWithCategoryDto> getPostsByTags(Pageable pageable, String q) {
        return postsRepository.getPostsByTags(pageable,q);
    }

    @Transactional
    public void addViews(Long id) {
        Post post = postsRepository.findById(id).orElseThrow(PostNotFound::new);
        post.addViews(1L);
    }

    public List<PostsResponseWithoutComment> getPopularPosts() {
        return postsRepository.getPopularPosts();
    }

    @Transactional(readOnly = true)
    public PostsResponse findPostsByIdIncludingComments(Long id) {
        return postsRepository.findByIdWithQdsl(id);
    }
}


