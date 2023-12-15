package com.blog.domain.facade;

import com.blog.service.OptimisticLockPostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OptimisticLockPostFacade {

    private final OptimisticLockPostService optimisticLockPostService;

    private static final int MAX_RETRIES=10;


    public void addViews(Long id) throws InterruptedException {
        int retries=0;

        while (retries < MAX_RETRIES) {
            try {
                optimisticLockPostService.addViews(id);

                break;
            } catch (ObjectOptimisticLockingFailureException e) {
                log.error("OptimisticLock 예외 발생: {}",e.getMessage());
                Thread.sleep(50);
                retries++;
            }
        }
        if (retries == MAX_RETRIES) {
            log.info("OptimisticLockPostFacade 최대 재시도 횟수 도달");
        }
    }
}
