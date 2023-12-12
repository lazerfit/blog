package com.blog.web.controller;

import com.blog.domain.user.Role;
import com.blog.domain.user.SiteUser;
import com.blog.domain.user.UserRepository;
import com.blog.web.form.UserRoleEditForm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

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

        UserRoleEditForm form = new UserRoleEditForm();
        form.setEmail(newUser.getEmail());
        form.setRole("관리자");

        mockMvc.perform(MockMvcRequestBuilders.post("/admin/setting/account/edit/role")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(form))
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk());

        SiteUser currentUser = userRepository.findAll().get(0);

        Assertions.assertThat(currentUser.getRole().toString()).hasToString("ADMIN");
    }
}
