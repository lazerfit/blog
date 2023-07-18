package com.blog.service;

import com.blog.domain.posts.Post;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.PostsNotFound;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.dto.PostsResponseWithCategoryDto;
import com.blog.web.dto.PostsResponseWithoutCommentDto;
import com.blog.web.dto.PostsSaveRequestDto;
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
    public void save(PostsSaveRequestDto request) {
        postsRepository.save(request.toEntity());
    }

    @Transactional
    public void edit(Long id, PostsUpdateRequestDto request) {
        postsRepository.edit(id,request);
    }

    @Transactional(readOnly = true)
    public PostsResponseDto findById(Long id) {
        Post post = postsRepository.findById(id)
            .orElseThrow(PostsNotFound::new);
        return new PostsResponseDto(post);
    }

    public PostsResponseWithCategoryDto findByIdWithCategory(Long id) {
        return postsRepository.findByIdContainCategory(id);
    }

    @Transactional(readOnly = true)
    public Page<PostsResponseWithoutCommentDto> getPosts(Pageable pageable) {
        return postsRepository.getPosts(pageable);
    }

    @Transactional
    public void delete(Long id) {
        Post post = postsRepository.findById(id).orElseThrow(PostsNotFound::new);
        postsRepository.delete(post);
    }

    public Page<PostsResponseDto> getSearchedPostsListByKeyword(Pageable pageable,String q) {
        return postsRepository.getPostsListByKeyword(pageable, q);
    }

    public Page<PostsResponseWithCategoryDto> getCategorizedPosts(Pageable pageable,
        String q) {
        return postsRepository.getCategorizedPosts(pageable, q);
    }

    public List<String> getTagsAsList(Long id) {
        Post post = postsRepository.findById(id).orElseThrow(PostsNotFound::new);
        String tags = post.getTags();
        return Stream.of(tags.split(",", -1)).toList();
    }

    public Page<PostsResponseWithCategoryDto> getPostsByTags(Pageable pageable, String q) {
        return postsRepository.getPostsByTags(pageable,q);
    }

    @Transactional
    public void addHit(Long id) {
        Post post = postsRepository.findById(id).orElseThrow(PostsNotFound::new);
        Long hit= post.getHit()+1L;
        post.updateHit(hit);
    }

    public List<PostsResponseWithoutCommentDto> getPopularPosts() {
        return postsRepository.getPopularPosts();
    }

    public List<PostsResponseDto> getCategorizedPostsNotContainPage(String q) {
        return postsRepository.getCategorizedPostsNotContainPage(
            q);
    }

    @Transactional(readOnly = true)
    public PostsResponseDto getPostsById(Long id) {
        return postsRepository.findByIdWithQdsl(id);
    }
}


