package com.blog.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SiteUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SITE_USER_ID")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email; // 아이디

    @Column(nullable = false)
    private String password;

    private LocalDateTime createdDate;

    @Builder
    public SiteUser(String name, String email, String password, LocalDateTime createdDate) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdDate = createdDate;
    }

    public void changePassword(String newPassword) {
        this.password=newPassword;
    }

}
