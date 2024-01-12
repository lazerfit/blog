package com.blog.service;

import com.blog.domain.recyclebin.RecycleBin;
import com.blog.domain.recyclebin.RecycleBinRepository;
import com.blog.exception.PostNotFound;
import com.blog.web.dto.RecycleBinResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecycleBinService {

    private final RecycleBinRepository recycleBinRepository;

    @Transactional
    public List<RecycleBinResponse> findAll() {
        return recycleBinRepository.findAll()
            .stream().map(RecycleBinResponse::new).toList();
    }

    @Transactional
    public void delete(Long postId) {
        RecycleBin recycleBinPost = recycleBinRepository.findById(postId)
            .orElseThrow(PostNotFound::new);

        recycleBinRepository.delete(recycleBinPost);
    }
}
