package com.blog.domain.posts;

import static com.blog.domain.posts.QPosts.posts;

import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class PostsRepositoryImpl implements PostsRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Posts> getPostsList(Pageable pageable) {
        List<Posts> postsList = jpaQueryFactory.selectFrom(posts)
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(posts.id.desc())
            .fetch();

        long totalCount = jpaQueryFactory.selectFrom(posts)
            .fetch().size();

        return new PageImpl<>(postsList,pageable,totalCount);

    }


}
