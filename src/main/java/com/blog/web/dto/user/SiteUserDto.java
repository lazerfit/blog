package com.blog.web.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class SiteUserDto {

    private final Long id;

    @NotBlank(message = "이메일은 필수입니다.")
    private final String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    private final String password;

    @NotBlank(message = "이름은 필수입니다.")
    private final String name;

    @Builder
    public SiteUserDto(Long id, String email, String password, String name) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
