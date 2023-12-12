package com.blog.domain.category;

import static com.blog.domain.category.QCategory.category;
import static com.blog.domain.posts.QPost.post;

import com.blog.web.dto.category.CategoryAndPostCreatedDateResponse;
import com.blog.web.dto.category.QCategoryAndPostCreatedDateResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CategoryAndPostCreatedDateResponse> getAllCategoryAndPostCreatedDate() {

        return jpaQueryFactory
            .select(
                new QCategoryAndPostCreatedDateResponse(
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
            .orderBy(category.listOrder.asc())
            .fetch();
    }
}
