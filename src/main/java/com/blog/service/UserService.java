package com.blog.service;

import com.blog.domain.user.SiteUser;
import com.blog.domain.user.UserRepository;
import com.blog.exception.PasswordNotMatches;
import com.blog.exception.UserNotFound;
import com.blog.web.dto.UserPasswordEditRequest;
import com.blog.web.dto.UserResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UserResponse> findAllUser() {
        List<SiteUser> allUsers = userRepository.findAll();
        return allUsers.stream().map(UserResponse::new).toList();
    }

    @Transactional
    public void changePassword(UserPasswordEditRequest passwordEdit) {
        SiteUser siteUser = userRepository.findByEmail(passwordEdit.getEmail())
            .orElseThrow(UserNotFound::new);

        if (passwordEncoder.matches(passwordEdit.getOriginPassword(), siteUser.getPassword())) {
            siteUser.changePassword(passwordEncoder.encode(passwordEdit.getNewPassword()));
        } else {
            throw new PasswordNotMatches();
        }
    }
}
