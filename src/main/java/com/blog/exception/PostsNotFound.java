package com.blog.exception;

public class PostsNotFound extends TopException{

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostsNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
