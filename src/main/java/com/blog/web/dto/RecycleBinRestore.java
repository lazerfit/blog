package com.blog.web.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class RecycleBinRestore {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;
    @NotBlank(message = "내용은 필수입니다.")
    private final String content;
    @NotBlank(message = "카테고리 선택은 필수입니다.")
    private final String category;
    private final String tags;
    private final Long views;

    private final String thumbnail;

    public RecycleBinRestore(String title, String content, String category, String tags,
        Long views,
        String thumbnail) {
        this.title = title;
        this.content = content;
        this.category = category;
        this.tags = tags;
        this.views = views;
        this.thumbnail = thumbnail;
    }
}
