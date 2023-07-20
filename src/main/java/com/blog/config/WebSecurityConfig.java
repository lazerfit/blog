package com.blog.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final String ROLE_ADMIN="ADMIN";

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/favicon.ico", "/error", "/img/**")
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/").permitAll()
                .requestMatchers(HttpMethod.GET, "/post/*").permitAll()
                .requestMatchers("/post/search/*").permitAll()
                .requestMatchers("/post/category/*").permitAll()
                .requestMatchers("/post/new").hasRole(ROLE_ADMIN)
                .requestMatchers("/post/delete/*").hasRole(ROLE_ADMIN)
                .requestMatchers("/post/edit/*").hasRole(ROLE_ADMIN)
                .requestMatchers("/post/admin/comment/delete/*").hasRole(ROLE_ADMIN)
                .requestMatchers("/post/comment/manage").permitAll()
                .requestMatchers("/post/comment/manage/edit").permitAll()
                .requestMatchers("/post/comment/subComment/new").permitAll()
                .requestMatchers("/post/comment/manage/delete").permitAll()
                .requestMatchers(HttpMethod.POST, "/post/comment/new").permitAll()
                .requestMatchers("/admin/**").permitAll()
                .requestMatchers("/auth/signup").permitAll()
                .requestMatchers("/auth/login").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .usernameParameter("username")
                .passwordParameter("password")
                .loginPage("/auth/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .permitAll()
            )
            .csrf(AbstractHttpConfigurer::disable)
            .rememberMe(rm -> rm.rememberMeParameter("remember")
                .alwaysRemember(false)
                .tokenValiditySeconds(2592000));
        return http.build();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
