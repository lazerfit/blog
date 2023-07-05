package com.blog.web.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryEditForm {

    @NotBlank(message = "제목은 필수입니다.") String title;
    @NotNull(message = "정렬 순서는 필수입니다.") Integer listOrder;

    public CategoryEditForm(String title, @NotNull(message = "정렬 순서는 필수입니다.") Integer listOrder) {
        this.title = title;
        this.listOrder = listOrder;
    }
}
