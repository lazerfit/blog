package com.blog.domain.posts;

import static com.blog.domain.posts.QPost.post;

import com.blog.web.dto.posts.PostsResponse;
import com.blog.web.dto.posts.QPostsResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Slf4j
@RequiredArgsConstructor
public class PostsRepositoryImpl implements PostsRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PostsResponse> getPosts(Pageable pageable) {
        List<PostsResponse> postsList = jpaQueryFactory
            .select(
                new QPostsResponse(
                    post.id,
                    post.title,
                    post.content,
                    post.generationTimeStamp,
                    post.category.title,
                    post.tag,
                    post.views,
                    post.thumbnail
                ))
            .from(post)
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(post.id.desc())
            .fetch();
        long totalCount = jpaQueryFactory.selectFrom(post)
            .fetch().size();
        return new PageImpl<>(postsList,pageable,totalCount);
    }

    @Override
    public Page<PostsResponse> getPostsByKeyword(Pageable pageable, String keyword) {
        List<PostsResponse> postsList = jpaQueryFactory.select(
                new QPostsResponse(post.id, post.title,post.content,post.generationTimeStamp,post.category.title,post.tag,post.views,post.thumbnail))
            .from(post)
            .where(post.title.contains(keyword))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(post.id.desc())
            .fetch();
        long totalCount=jpaQueryFactory.selectFrom(post)
            .where(post.title.contains(keyword)).fetch().size();
        return new PageImpl<>(postsList,pageable,totalCount);
    }

    @Override
    public Page<PostsResponse> getPostsSortedByCategory(Pageable pageable,
        String q) {
        List<PostsResponse> postsList = jpaQueryFactory
            .select(
                new QPostsResponse(
                    post.id,
                    post.title,
                    post.content,
                    post.generationTimeStamp,
                    post.category.title,
                    post.tag,
                    post.views,
                    post.thumbnail
                ))
            .from(post)
            .where(post.category.title.eq(q))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(post.id.desc())
            .fetch();
        long totalCount=jpaQueryFactory.selectFrom(post)
            .where(post.category.title.eq(q)).fetch().size();
        return new PageImpl<>(postsList,pageable,totalCount);
    }

    @Override
    public Page<PostsResponse> getPostsByTag(Pageable pageable, String tag) {
        List<PostsResponse> response = jpaQueryFactory
            .select(
                new QPostsResponse(
                    post.id,
                    post.title,
                    post.content,
                    post.generationTimeStamp,
                    post.category.title,
                    post.tag,
                    post.views,
                    post.thumbnail
                ))
            .from(post)
            .where(post.tag.contains(tag))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(post.id.desc())
            .fetch();
        long totalCount=jpaQueryFactory.selectFrom(post).where(post.tag.contains(tag)).fetch().size();
        return new PageImpl<>(response, pageable, totalCount);
    }

    @Override
    public List<PostsResponse> getPopularPosts() {
        return jpaQueryFactory
            .select(
                new QPostsResponse(
                    post.id,
                    post.title,
                    post.content,
                    post.generationTimeStamp,
                    post.category.title,
                    post.tag,
                    post.views,
                    post.thumbnail
                ))
            .from(post)
            .orderBy(post.views.desc())
            .limit(3)
            .fetch();
    }

    @Override
    public List<PostsResponse> getPostsSortedByCategory(String q) {
        return jpaQueryFactory.select(new QPostsResponse(
                post.id,
                post.title,
                post.content,
                post.generationTimeStamp,
                post.category.title,
                post.tag,
                post.views,
                post.thumbnail
            ))
            .from(post)
            .where(post.category.title.eq(q))
            .orderBy(post.generationTimeStamp.desc())
            .limit(6)
            .fetch();
    }

    @Override
    public Optional<PostsResponse> getPostById(Long postId) {
        PostsResponse response = jpaQueryFactory.select(
                new QPostsResponse(
                    post.id,
                    post.title,
                    post.content,
                    post.generationTimeStamp,
                    post.category.title,
                    post.tag,
                    post.views,
                    post.thumbnail
                ))
            .from(post)
            .where(post.id.eq(postId))
            .fetchOne();
        return Optional.ofNullable(response);
    }
}
