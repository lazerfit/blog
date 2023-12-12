package com.blog.service;

import com.blog.domain.user.Role;
import com.blog.domain.user.SiteUser;
import com.blog.domain.user.UserRepository;
import com.blog.exception.PasswordNotMatches;
import com.blog.exception.UserNotFound;
import com.blog.web.dto.UserPasswordEditRequest;
import com.blog.web.dto.UserResponse;
import com.blog.web.form.UserRoleEditForm;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
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

    @Transactional
    public void editRole(UserRoleEditForm form) {
        log.info(form.getEmail());
        SiteUser siteUser = userRepository.findByEmail(form.getEmail())
            .orElseThrow(UserNotFound::new);
        if (form.getRole().equals("관리자")) {
            siteUser.editRole(Role.ADMIN);
        } else {
            siteUser.editRole(Role.USER);
        }
    }
}
