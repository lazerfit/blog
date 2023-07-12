package com.blog.domain.comments;

import static com.blog.domain.comments.QComment.comment;
import static com.blog.domain.posts.QPosts.posts;

import com.blog.web.dto.CommentsResponseDto;
import com.blog.web.dto.QCommentsResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<CommentsResponseDto> findByPostsId(Long postId) {

        List<CommentsResponseDto> parentComment = jpaQueryFactory.select(
                new QCommentsResponseDto(
                    comment.id,
                    comment.username,
                    comment.content,
                    comment.createDate,
                    comment.parent.id)
            )
            .from(comment)
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
            .where(posts.id.eq(postId).and(comment.parent.id.isNotNull()))
            .orderBy(comment.id.asc())
            .fetch();

        parentComment.forEach(p-> p.insertChildComment(childComment.stream().filter(c->c.getParentId().equals(p.getId())).toList()));

        return parentComment;
    }
}
