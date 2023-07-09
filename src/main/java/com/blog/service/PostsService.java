package com.blog.service;

import com.blog.domain.posts.Posts;
import com.blog.domain.posts.PostsRepository;
import com.blog.exception.PostsNotFound;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.dto.PostsResponseWithCategoryDto;
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

    public PostsResponseDto findById(Long id) {
        Posts posts = postsRepository.findById(id)
            .orElseThrow(PostsNotFound::new);
        return new PostsResponseDto(posts);
    }

    public PostsResponseWithCategoryDto findByIdWithCategory(Long id) {
        return postsRepository.findByIdContainCategory(id);
    }

    @Transactional(readOnly = true)
    public Page<PostsResponseDto> getPostsList(Pageable pageable) {
        return postsRepository.getPostsList(pageable);
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(PostsNotFound::new);
        postsRepository.delete(posts);
    }

    public Page<PostsResponseDto> getSearchedPostsListByKeyword(Pageable pageable,String q) {
        return postsRepository.getPostsListByKeyword(pageable, q);
    }

    public Page<PostsResponseWithCategoryDto> getCategorizedPosts(Pageable pageable,
        String q) {
        return postsRepository.getCategorizedPosts(pageable, q);
    }

    public List<String> getTagsAsList(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(PostsNotFound::new);
        String tags = posts.getTags();
        return Stream.of(tags.split(",", -1)).toList();
    }

    public Page<PostsResponseWithCategoryDto> getPostsByTags(Pageable pageable, String q) {
        return postsRepository.getPostsByTags(pageable,q);
    }

    @Transactional
    public void addHit(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(PostsNotFound::new);
        Long hit=posts.getHit()+1L;
        posts.updateHit(hit);
    }

    public List<PostsResponseDto> getPopularPosts() {
        List<Posts> popularPosts = postsRepository.getPopularPosts();
        return popularPosts.stream().map(PostsResponseDto::new).toList();
    }

    public List<PostsResponseDto> getCategorizedPostsNotContainPage(String q) {
        List<Posts> categorizedPostsNotContainPage = postsRepository.getCategorizedPostsNotContainPage(
            q);
        return categorizedPostsNotContainPage.stream().map(PostsResponseDto::new).toList();
    }
}


