package com.blog.web.controller;

import com.blog.domain.user.Role;
import com.blog.domain.user.SiteUser;
import com.blog.domain.user.UserRepository;
import com.blog.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@WithMockUser(roles = "ADMIN")
@AutoConfigureMockMvc
class AdminManageControllerTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void editRole() throws Exception {

        SiteUser newUser = userRepository.save(SiteUser.builder()
            .email("Abc@abc.com")
            .name("mang")
            .role(Role.USER)
            .password(passwordEncoder.encode("1234"))
            .build()
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/setting/account/edit/role")
                .param("email", newUser.getEmail())
                .param("role", "관리자")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk());

        SiteUser currentUser = userRepository.findAll().get(0);

        Assertions.assertThat(currentUser.getRole().toString()).hasToString("ADMIN");
    }
}
