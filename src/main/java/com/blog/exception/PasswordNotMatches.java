package com.blog.exception;

public class PasswordNotMatches extends TopException{

    private static final String MESSAGE = "비밀번호가 올바르지 않습니다.";

    public PasswordNotMatches() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
