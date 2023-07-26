package com.blog.domain.posts;

import static com.blog.domain.comments.QComment.comment;
import static com.blog.domain.posts.QPost.post;

import com.blog.exception.PostNotFound;
import com.blog.web.dto.comments.CommentsResponse;
import com.blog.web.dto.comments.QCommentsResponse;
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
    public Page<PostsResponse> fetchPostsExcludingComment(Pageable pageable) {
        List<PostsResponse> postsList = jpaQueryFactory
            .select(
                new QPostsResponse(
                    post.id,
                    post.title,
                    post.content,
                    post.generationTimeStamp,
                    post.category.title,
                    post.tag,
                    post.views
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
    public Page<PostsResponse> findPostsByKeyword(Pageable pageable, String keyword) {
        List<PostsResponse> postsList = jpaQueryFactory.select(
                new QPostsResponse(post.id, post.title,post.content,post.generationTimeStamp,post.category.title,post.tag,post.views))
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
    public Page<PostsResponse> findPostsSortedByCategory(Pageable pageable,
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
                    post.views
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
    public Page<PostsResponse> findPostsByTag(Pageable pageable, String tag) {
        List<PostsResponse> response = jpaQueryFactory
            .select(
                new QPostsResponse(
                    post.id,
                    post.title,
                    post.content,
                    post.generationTimeStamp,
                    post.category.title,
                    post.tag,
                    post.views
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
                    post.views
                ))
            .from(post)
            .orderBy(post.views.desc())
            .limit(3)
            .fetch();
    }

    @Override
    public List<PostsResponse> findPostsSortedByCategory(String q) {
        return jpaQueryFactory.select(new QPostsResponse(
                post.id,
                post.title,
                post.content,
                post.generationTimeStamp,
                post.category.title,
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
    public PostsResponse findPostsByIdIncludingComments(Long postId) {
        PostsResponse postsResponse = jpaQueryFactory
            .select(new QPostsResponse(
                post.id,
                post.title,
                post.content,
                post.generationTimeStamp,
                post.category.title,
                post.tag,
                post.views))
            .from(post)
            .where(post.id.eq(postId))
            .fetchOne();

        List<CommentsResponse> parentComment = jpaQueryFactory
            .select(new QCommentsResponse(
                comment.id,
                comment.username,
                comment.content,
                comment.generationTimeStamp,
                comment.parent.id))
            .from(comment)
            .where(post.id.eq(postId).and(comment.parent.id.isNull()))
            .orderBy(comment.id.asc())
            .fetch();

        List<CommentsResponse> childComment = jpaQueryFactory
            .select(new QCommentsResponse(
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
        if (postsResponse == null) {
            throw new PostNotFound();
        }
        postsResponse.insertComment(parentComment);
        return postsResponse;
    }

    @Override
    public Optional<PostsResponse> findPostById(Long postId) {
        PostsResponse response = jpaQueryFactory.select(
                new QPostsResponse(
                    post.id,
                    post.title,
                    post.content,
                    post.generationTimeStamp,
                    post.category.title,
                    post.tag,
                    post.views
                ))
            .from(post)
            .where(post.id.eq(postId))
            .fetchOne();
        return Optional.ofNullable(response);
    }
}
