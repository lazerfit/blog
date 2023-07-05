package com.blog.web.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryForm {

    @NotBlank(message = "카테고리 명은 필수입니다.")
    private String title;
    @NotBlank(message = "목록 순서는 필수입니다.")
    private Integer listOrder;

}
