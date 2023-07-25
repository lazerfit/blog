package com.blog.web.dto.posts;

import com.blog.domain.category.Category;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class PostsUpdateRequest {

    private final String title;
    private final String content;
    private final String tag;
    private final Category category;

    @Builder
    public PostsUpdateRequest(String title, String content, String tag, Category category) {
        this.title = title;
        this.content = content;
        this.tag = nullCheckAndSetDefaultValue(tag);
        this.category = category;
    }

    private String nullCheckAndSetDefaultValue(String tags){
        if (isTagNotValid(tags)) {
            return "없음";
        }
        return this.transFormJsonArrayToString(tags);
    }

    private static boolean isTagNotValid(String tags) {
        return tags == null || tags.isBlank();
    }

    private String transFormJsonArrayToString(String tags){
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> tagList=new ArrayList<>();
        try{
            JsonNode rootNode = objectMapper.readTree(tags);
            for (JsonNode node : rootNode) {
                String value = node.get("value").asText();
                tagList.add(value);
            }
        } catch(Exception e) {
            log.info(String.valueOf(e));
        }
        return String.join(",", tagList);
    }
}
