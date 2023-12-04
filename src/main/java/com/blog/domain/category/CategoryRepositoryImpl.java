package com.blog.domain.category;

import static com.blog.domain.category.QCategory.category;
import static com.blog.domain.posts.QPost.post;

import com.blog.web.dto.category.CategoryAndPostCreatedDateResponse;
import com.blog.web.dto.posts.PostsResponse;
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
public class CategoryRepositoryImpl implements CategoryRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PostsResponse> getCategorizedPostsList(Pageable pageable,String q) {
        List<PostsResponse> categorizedPostsList = jpaQueryFactory.select(
                Projections.constructor(PostsResponse.class,post.id,post.title,post.content,post.generationTimeStamp))
            .from(post)
            .where(category.title.eq(q))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(post.id.desc())
            .fetch();

        long totalCount=jpaQueryFactory.selectFrom(post)
            .where(QCategory.category.title.eq(q)).fetch().size();

        return new PageImpl<>(categorizedPostsList, pageable, totalCount);
    }

    @Override
    public List<CategoryAndPostCreatedDateResponse> getAllCategoryAndPostCreatedDate() {

        return jpaQueryFactory
            .select(
                Projections.constructor(
                    CategoryAndPostCreatedDateResponse.class,
                    category.id,
                    category.listOrder,
                    category.title,
                    post.generationTimeStamp.max()
                )
            )
            .from(category)
            .leftJoin(post)
            .on(category.id.eq(post.category.id))
            .groupBy(category.id, category.title)
            .fetch();
    }
}
