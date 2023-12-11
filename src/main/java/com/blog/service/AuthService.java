package com.blog.service;

import com.blog.domain.user.Role;
import com.blog.domain.user.SiteUser;
import com.blog.domain.user.UserRepository;
import com.blog.exception.AlreadyExistsEmailException;
import com.blog.web.dto.SiteUserDto;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void signUp(SiteUserDto siteUserDto) {
        Optional<SiteUser> siteUserOptional = userRepository.findByEmail(siteUserDto.getEmail());
        if (siteUserOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = passwordEncoder.encode(siteUserDto.getPassword());

        var user=SiteUser.builder()
            .email(siteUserDto.getEmail())
            .name(siteUserDto.getName())
            .password(encryptedPassword)
            .createdDate(LocalDateTime.now())
            .role(Role.USER)
            .build();

        userRepository.save(user);
    }

}
