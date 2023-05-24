package com.blog.domain.posts;

import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
@Slf4j
@RequiredArgsConstructor
public class TestDataInit {

    private final PostsRepository postsRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");
        List<Posts> requestDtoList = IntStream.range(0, 30)
            .mapToObj(i ->
                Posts.builder()
                    .title("title" + i)
                    .content("content" + i)
                    .build())
            .toList();

        postsRepository.saveAll(requestDtoList);
    }
}
