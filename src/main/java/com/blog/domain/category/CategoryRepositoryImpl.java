package com.blog.domain.category;

import static com.blog.domain.category.QCategory.category;
import static com.blog.domain.posts.QPosts.posts;

import com.blog.web.dto.PostsResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PostsResponseDto> getCategorizedPostsList(Pageable pageable,String q) {
        List<PostsResponseDto> categorizedPostsList = jpaQueryFactory.select(
                Projections.constructor(PostsResponseDto.class,posts.id,posts.title,posts.content,posts.createDate))
            .from(posts)
            .where(category.title.eq(q))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(posts.id.desc())
            .fetch();

        long totalCount=jpaQueryFactory.selectFrom(posts)
            .where(QCategory.category.title.eq(q)).fetch().size();

        return new PageImpl<>(categorizedPostsList, pageable, totalCount);
    }
}
