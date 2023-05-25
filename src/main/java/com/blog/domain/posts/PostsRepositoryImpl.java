package com.blog.domain.posts;

import static com.blog.domain.posts.QPosts.posts;

import com.blog.web.dto.PostsSearchRequestDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PostsRepositoryImpl implements PostsRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Posts> getPostsList(PostsSearchRequestDto request) {
        return jpaQueryFactory.selectFrom(posts)
            .limit(request.getSize())
            .offset(request.getOffset())
            .orderBy(posts.id.desc())
            .fetch();
    }
}
