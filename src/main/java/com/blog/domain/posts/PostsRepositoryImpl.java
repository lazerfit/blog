package com.blog.domain.posts;

import static com.blog.domain.comments.QComment.comment;
import static com.blog.domain.posts.QPosts.posts;

import com.blog.exception.PostsNotFound;
import com.blog.web.dto.CommentsResponseDto;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.dto.PostsResponseWithCategoryDto;
import com.blog.web.dto.PostsUpdateRequestDto;
import com.blog.web.dto.QCommentsResponseDto;
import com.blog.web.dto.QPostsResponseDto;
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
            .select(new QPostsResponseDto(posts.id, posts.title,posts.content,posts.createDate,posts.category,posts.tags,posts.hit))
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
                new QPostsResponseDto(posts.id, posts.title,posts.content,posts.createDate,posts.category,posts.tags,posts.hit))
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

    @Override
    public PostsResponseWithCategoryDto findByIdContainCategory(Long id) {
        List<PostsResponseWithCategoryDto> response = jpaQueryFactory.select(
                Projections.constructor(PostsResponseWithCategoryDto.class, posts.id,
                    posts.title, posts.content, posts.createDate, posts.category.title,posts.tags,posts.hit))
            .from(posts)
            .where(posts.id.eq(id))
            .fetch();

        return response.get(0);
    }

    @Override
    public Page<PostsResponseWithCategoryDto> getCategorizedPosts(Pageable pageable,
        String q) {
        List<PostsResponseWithCategoryDto> postsList = jpaQueryFactory.select(
                Projections.constructor(PostsResponseWithCategoryDto.class, posts.id, posts.title,
                    posts.content, posts.createDate, posts.category.title,posts.tags,posts.hit))
            .from(posts)
            .where(posts.category.title.eq(q))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(posts.id.desc())
            .fetch();

        long totalCount=jpaQueryFactory.selectFrom(posts)
            .where(posts.category.title.eq(q)).fetch().size();

        return new PageImpl<>(postsList,pageable,totalCount);
    }

    @Override
    public void edit(Long id, PostsUpdateRequestDto requestDto) {
        jpaQueryFactory.update(posts)
            .where(posts.id.eq(id))
            .set(posts.title, requestDto.title())
            .set(posts.content,requestDto.content())
            .set(posts.category,requestDto.category())
            .execute();
    }

    @Override
    public Page<PostsResponseWithCategoryDto> getPostsByTags(Pageable pageable, String tag) {
        List<PostsResponseWithCategoryDto> response = jpaQueryFactory.select(
                Projections.constructor(PostsResponseWithCategoryDto.class, posts.id, posts.title,
                    posts.content, posts.createDate, posts.category.title, posts.tags,posts.hit))
            .from(posts)
            .where(posts.tags.contains(tag))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .orderBy(posts.id.desc())
            .fetch();

        long totalCount=jpaQueryFactory.selectFrom(posts).where(posts.tags.contains(tag)).fetch().size();

        return new PageImpl<>(response, pageable, totalCount);
    }

    @Override
    public List<Posts> getPopularPosts() {
        return jpaQueryFactory.selectFrom(posts)
            .orderBy(posts.hit.desc())
            .limit(3)
            .fetch();
    }

    @Override
    public List<PostsResponseDto> getCategorizedPostsNotContainPage(String q) {
        return jpaQueryFactory.select(new QPostsResponseDto(
                posts.id,
                posts.title,
                posts.content,
                posts.createDate,
                posts.category,
                posts.tags,
                posts.hit))
            .from(posts)
            .where(posts.category.title.eq(q))
            .orderBy(posts.createDate.desc())
            .limit(6)
            .fetch();
    }

    @Override
    public PostsResponseDto findByIdWithQdsl(Long postId) {
        PostsResponseDto postsResponseDto = jpaQueryFactory
            .select(new QPostsResponseDto(
                posts.id,
                posts.title,
                posts.content,
                posts.createDate,
                posts.category,
                posts.tags,
                posts.hit))
            .from(posts)
            .where(posts.id.eq(postId))
            .fetchOne();

        List<CommentsResponseDto> parentComment = jpaQueryFactory
            .select(new QCommentsResponseDto(
                comment.id,
                comment.username,
                comment.content,
                comment.createDate,
                comment.parent.id))
            .from(comment)
//            .join(comment.posts, posts)
            .where(posts.id.eq(postId).and(comment.parent.id.isNull()))
            .orderBy(comment.id.asc())
            .fetch();

        List<CommentsResponseDto> childComment = jpaQueryFactory
            .select(new QCommentsResponseDto(
                comment.id,
                comment.username,
                comment.content,
                comment.createDate,
                comment.parent.id))
            .from(comment)
//            .join(comment.posts, posts)
            .where(posts.id.eq(postId).and(comment.parent.id.isNotNull()))
            .orderBy(comment.id.asc())
            .fetch();

        parentComment.forEach(p-> p.insertChildComment(childComment.stream().filter(c->c.getParentId().equals(p.getId())).toList()));

        if (postsResponseDto == null) {
            throw new PostsNotFound();
        }

        postsResponseDto.insertComment(parentComment);

        return postsResponseDto;

    }

}
