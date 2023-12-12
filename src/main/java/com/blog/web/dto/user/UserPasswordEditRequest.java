package com.blog.web.dto.user;

import lombok.Builder;
import lombok.Getter;

@Getter
public class UserPasswordEditRequest {

    private String email;
    private String originPassword;
    private String newPassword;

    @Builder
    public UserPasswordEditRequest(String email, String originPassword, String newPassword) {
        this.email = email;
        this.originPassword = originPassword;
        this.newPassword = newPassword;
    }
}
