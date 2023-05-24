package com.blog;

import com.blog.domain.posts.PostsRepository;
import com.blog.domain.posts.TestDataInit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    @Bean
    @Profile("local")
    public TestDataInit testDataInit(PostsRepository postsRepository) {
        return new TestDataInit(postsRepository);
    }
}
