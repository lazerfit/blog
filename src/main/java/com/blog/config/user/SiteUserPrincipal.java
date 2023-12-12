package com.blog.config.user;

import com.blog.domain.user.SiteUser;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

@Getter
public class SiteUserPrincipal extends User {


    public SiteUserPrincipal(SiteUser user) {
        super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

}
