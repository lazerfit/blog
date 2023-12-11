package com.blog.web.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {

    @NotNull
    private String email;
    @NotNull
    private String password;
}
