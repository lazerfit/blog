package com.blog.domain.posts;

import static com.blog.domain.posts.QPosts.posts;

import com.blog.web.dto.PostsResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
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
    public Page<PostsResponseDto> getPostsList(Pageable pageable) {
        List<PostsResponseDto> postsList = jpaQueryFactory
            .select( Projections.constructor(PostsResponseDto.class,posts.id,posts.title,posts.content,posts.createDate))
            .from(posts)
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(posts.id.desc())
            .fetch();
        long totalCount = jpaQueryFactory.selectFrom(posts)
            .fetch().size();
        return new PageImpl<>(postsList,pageable,totalCount);
    }

    @Override
    public Page<PostsResponseDto> getPostsListByKeyword(Pageable pageable, String keyword) {
        List<PostsResponseDto> postsList = jpaQueryFactory.select(
                Projections.constructor(PostsResponseDto.class,posts.id,posts.title,posts.content,posts.createDate))
            .from(posts)
            .where(posts.title.contains(keyword))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(posts.id.desc())
            .fetch();
        long totalCount=jpaQueryFactory.selectFrom(posts)
            .where(posts.title.contains(keyword)).fetch().size();

        return new PageImpl<>(postsList,pageable,totalCount);
    }
}
