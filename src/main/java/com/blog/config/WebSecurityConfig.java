package com.blog.config;

import com.blog.domain.user.UserRepository;
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

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/favicon.ico", "/error", "/img/**")
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
        UserRepository userRepository) throws Exception {
        http
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/").permitAll()
                .requestMatchers(HttpMethod.GET,"/posts/*").permitAll()
                .requestMatchers("/posts/search/*").permitAll()
                .requestMatchers("/posts/new").hasRole("ADMIN")
                .requestMatchers("/posts/delete/*").hasRole("ADMIN")
                .requestMatchers("/posts/edit/*").hasRole("ADMIN")
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
//            .logout(LogoutConfigurer::permitAll)
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
