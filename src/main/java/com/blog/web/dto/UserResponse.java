package com.blog.web.dto;

import com.blog.domain.user.SiteUser;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * DTO for {@link com.blog.domain.user.SiteUser}
 */
@Getter
@RequiredArgsConstructor
public class UserResponse implements Serializable {

    private final String name;
    private final String email;
    private final LocalDateTime createdDate;
    private final String role;

    public UserResponse(SiteUser user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.createdDate = user.getCreatedDate();
        this.role=user.getRoleKey();
    }
}
