package com.skateshop.dto.response;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryPostResponse {

    private String categoryName;

    private Long id;
}
