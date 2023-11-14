package com.blog.config;

import com.blog.config.filter.EmailPasswordAuthFilter;
import com.blog.config.handler.Http401Handler;
import com.blog.config.handler.LoginFailHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private static final String ROLE_ADMIN="ADMIN";
    private final ObjectMapper objectMapper;
    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/favicon.ico", "/error", "/img/**")
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public EmailPasswordAuthFilter usernamePasswordAuthenticationFilter() {
        EmailPasswordAuthFilter filter
            = new EmailPasswordAuthFilter("/auth/login",objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        filter.setAuthenticationSuccessHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
        filter.setAuthenticationFailureHandler(new LoginFailHandler(objectMapper));
        filter.setSecurityContextRepository(new HttpSessionSecurityContextRepository());

        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        rememberMeServices.setValiditySeconds(3600 * 24 * 30);
        filter.setRememberMeServices(rememberMeServices);
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
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
                .requestMatchers("/tag").permitAll()
                .requestMatchers("/post/admin/comment/delete/*").hasRole(ROLE_ADMIN)
                .requestMatchers("/post/comment/manage").permitAll()
                .requestMatchers("/post/comment/manage/edit").permitAll()
                .requestMatchers("/post/comment/subComment/new").permitAll()
                .requestMatchers("/post/comment/manage/delete").permitAll()
                .requestMatchers(HttpMethod.POST, "/post/comment/new").permitAll()
                .requestMatchers("/admin/**").hasRole(ROLE_ADMIN)
                .requestMatchers("/auth/signup").permitAll()
                .requestMatchers("/auth/login").permitAll()
                .requestMatchers("/login").permitAll()
                .anyRequest().authenticated()
            )
            .addFilterBefore(usernamePasswordAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class)
            .exceptionHandling(e -> {
                e.authenticationEntryPoint(new Http401Handler(objectMapper));
            })
//            .formLogin(form -> form
//                .usernameParameter("username")
//                .passwordParameter("password")
//                .loginPage("/auth/login")
//                .loginProcessingUrl("/login")
//                .defaultSuccessUrl("/")
//                .permitAll()
//            )
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
