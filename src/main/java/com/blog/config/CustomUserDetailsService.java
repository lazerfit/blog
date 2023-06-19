package com.blog.config;

import com.blog.domain.user.SiteUser;
import com.blog.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        SiteUser siteUser = userRepository.findByEmail(email).orElseThrow(
            () -> new UsernameNotFoundException("존재하지않는 아이디 입니다."));

        if (email.equals("unpadded7479@duck.com")) {
            return new SiteUserPrincipal(siteUser);
        } else {
            return new SiteUserSecondary(siteUser);
        }
    }
}
