package com.blog.domain.posts;

import static com.blog.domain.comments.QComment.comment;
import static com.blog.domain.posts.QPost.post;

import com.blog.exception.PostNotFound;
import com.blog.web.dto.CommentsResponseDto;
import com.blog.web.dto.PostsResponseDto;
import com.blog.web.dto.PostsResponseWithCategoryDto;
import com.blog.web.dto.PostsResponseWithoutCommentDto;
import com.blog.web.dto.PostsUpdateRequestDto;
import com.blog.web.dto.QCommentsResponseDto;
import com.blog.web.dto.QPostsResponseDto;
import com.blog.web.dto.QPostsResponseWithoutCommentDto;
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
    public Page<PostsResponseWithoutCommentDto> getPostsWithPaging(Pageable pageable) {
        List<PostsResponseWithoutCommentDto> postsList = jpaQueryFactory
            .select(new QPostsResponseWithoutCommentDto(post.id, post.title, post.content,
                post.generationTimeStamp, post.category, post.tag, post.views))
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
    public Page<PostsResponseDto> getPostsListByKeyword(Pageable pageable, String keyword) {
        List<PostsResponseDto> postsList = jpaQueryFactory.select(
                new QPostsResponseDto(post.id, post.title,post.content,post.generationTimeStamp,post.category,post.tag,post.views))
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
    public PostsResponseWithCategoryDto findByIdContainCategory(Long id) {
        List<PostsResponseWithCategoryDto> response = jpaQueryFactory.select(
                Projections.constructor(PostsResponseWithCategoryDto.class, post.id,
                    post.title, post.content, post.generationTimeStamp, post.category.title,post.tag,post.views))
            .from(post)
            .where(post.id.eq(id))
            .fetch();

        return response.get(0);
    }

    @Override
    public Page<PostsResponseWithCategoryDto> getCategorizedPosts(Pageable pageable,
        String q) {
        List<PostsResponseWithCategoryDto> postsList = jpaQueryFactory.select(
                Projections.constructor(PostsResponseWithCategoryDto.class, post.id, post.title,
                    post.content, post.generationTimeStamp, post.category.title,post.tag,post.views))
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
    public void edit(Long id, PostsUpdateRequestDto requestDto) {
        jpaQueryFactory.update(post)
            .where(post.id.eq(id))
            .set(post.title, requestDto.title())
            .set(post.content,requestDto.content())
            .set(post.category,requestDto.category())
            .execute();
    }

    @Override
    public Page<PostsResponseWithCategoryDto> getPostsByTags(Pageable pageable, String tag) {
        List<PostsResponseWithCategoryDto> response = jpaQueryFactory.select(
                Projections.constructor(PostsResponseWithCategoryDto.class, post.id, post.title,
                    post.content, post.generationTimeStamp, post.category.title, post.tag,post.views))
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
    public List<PostsResponseWithoutCommentDto> getPopularPosts() {
        return jpaQueryFactory.select(new QPostsResponseWithoutCommentDto(post.id, post.title, post.content,
                post.generationTimeStamp, post.category, post.tag, post.views))
            .from(post)
            .orderBy(post.views.desc())
            .limit(3)
            .fetch();
    }

    @Override
    public List<PostsResponseDto> getCategorizedPostsNotContainPage(String q) {
        return jpaQueryFactory.select(new QPostsResponseDto(
                post.id,
                post.title,
                post.content,
                post.generationTimeStamp,
                post.category,
                post.tag,
                post.views
            ))
            .from(post)
            .where(post.category.title.eq(q))
            .orderBy(post.generationTimeStamp.desc())
            .limit(6)
            .fetch();
    }

    @Override
    public PostsResponseDto findByIdWithQdsl(Long postId) {
        PostsResponseDto postsResponseDto = jpaQueryFactory
            .select(new QPostsResponseDto(
                post.id,
                post.title,
                post.content,
                post.generationTimeStamp,
                post.category,
                post.tag,
                post.views))
            .from(post)
            .where(post.id.eq(postId))
            .fetchOne();

        List<CommentsResponseDto> parentComment = jpaQueryFactory
            .select(new QCommentsResponseDto(
                comment.id,
                comment.username,
                comment.content,
                comment.generationTimeStamp,
                comment.parent.id))
            .from(comment)
            .where(post.id.eq(postId).and(comment.parent.id.isNull()))
            .orderBy(comment.id.asc())
            .fetch();

        List<CommentsResponseDto> childComment = jpaQueryFactory
            .select(new QCommentsResponseDto(
                comment.id,
                comment.username,
                comment.content,
                comment.generationTimeStamp,
                comment.parent.id))
            .from(comment)
            .where(post.id.eq(postId).and(comment.parent.id.isNotNull()))
            .orderBy(comment.id.asc())
            .fetch();

        parentComment.forEach(p-> p.insertChildComment(childComment.stream().filter(c->c.getParentId().equals(p.getId())).toList()));

        if (postsResponseDto == null) {
            throw new PostNotFound();
        }

        postsResponseDto.insertComment(parentComment);

        return postsResponseDto;
    }

}
