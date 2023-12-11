package com.blog.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.blog.config.QdslConfig;
import com.blog.domain.user.Role;
import com.blog.domain.user.SiteUser;
import com.blog.domain.user.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import(QdslConfig.class)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void makeMockUser() {
        userRepository.save(SiteUser.builder()
            .email("Abc@abc.com")
            .name("mang")
            .password(new BCryptPasswordEncoder().encode("1234"))
            .role(Role.USER)
            .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void editRole() {
        SiteUser siteUser = userRepository.findAll().get(0);

        siteUser.editRole(Role.ADMIN);

        SiteUser siteUser2 = userRepository.findAll().get(0);

        assertThat(siteUser2.getRole().toString()).hasToString("ADMIN");
    }
}
