package com.blog.config.user;

import com.blog.domain.user.SiteUser;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class SiteUserSecondary extends User {

    public SiteUserSecondary(SiteUser user) {
        super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
