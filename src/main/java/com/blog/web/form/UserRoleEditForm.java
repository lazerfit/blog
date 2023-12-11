package com.blog.web.form;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleEditForm {

    @NotNull
    private String role;
    @NotNull
    private String email;
}
