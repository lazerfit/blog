package com.blog.web.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.blog.domain.user.SiteUser;
import com.blog.domain.user.UserRepository;
import com.blog.service.UserService;
import com.blog.web.dto.UserPasswordEditRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@WithMockUser(roles = "ADMIN")
@AutoConfigureMockMvc
class AdminManageControllerTest {

    @Autowired
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;
    @BeforeEach
    void makeMockUser() {
        userRepository.save(SiteUser.builder()
            .email("abc@abc.com")
            .password(passwordEncoder.encode("1234"))
            .name("abc")
            .build());
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void changePassword() throws Exception {
        UserPasswordEditRequest editRequest = UserPasswordEditRequest.builder()
            .email("abc@abc.com")
            .originPassword("1234")
            .newPassword("12345")
            .build();

        mockMvc.perform(post("/admin/setting/account/edit/password")
                .with(csrf())
            .content(objectMapper.writeValueAsString(editRequest))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

    }
}
