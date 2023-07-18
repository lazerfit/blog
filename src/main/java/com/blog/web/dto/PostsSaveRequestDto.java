package com.blog.web.dto;

import com.blog.domain.category.Category;
import com.blog.domain.posts.Post;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PostsSaveRequestDto {

    @NotBlank(message = "제목은 필수입니다.")
    private final String title;

    @NotBlank(message = "내용은 필수입니다.")
    private final String content;

    @NotBlank(message = "카테고리 선택은 필수입니다.")
    private final Category category;

    private final String tags;

    private final Long hit;

    public Post toEntity(){
        return Post.builder()
            .title(title)
            .content(content)
            .category(category)
            .tags(this.nullCheckAndSetDefaultValue(tags))
            .hit(hit)
            .build();
    }

    private String nullCheckAndSetDefaultValue(String tags){
        if (tags == null || tags.isBlank()) {
            return "없음";
        }
        return this.transFormJsonArrayToString(tags);
    }

    private String transFormJsonArrayToString(String tags){
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> tagList=new ArrayList<>();
        try{JsonNode rootNode = objectMapper.readTree(tags);

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
