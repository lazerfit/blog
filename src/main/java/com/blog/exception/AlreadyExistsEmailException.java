package com.blog.exception;

public class AlreadyExistsEmailException extends TopException {

    private static final String MESSAGE = "이미 존재하는 이메일입니다.";

    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
