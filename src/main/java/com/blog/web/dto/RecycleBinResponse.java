package com.blog.web.dto;

import com.blog.domain.recyclebin.RecycleBin;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class RecycleBinResponse {

    private final Long id;
    private final String title;
    private final LocalDateTime deletedDate;
    private final String content;
    private final String category;
    private final String tag;
    private final String thumbnail;

    public RecycleBinResponse(RecycleBin entity) {
        this.id=entity.getId();
        this.title=entity.getTitle();
        this.deletedDate=entity.getGenerationTimeStamp();
        this.content=entity.getContent();
        this.category=entity.getCategory();
        this.tag=entity.getTag();
        this.thumbnail=entity.getThumbnail();
    }
}
