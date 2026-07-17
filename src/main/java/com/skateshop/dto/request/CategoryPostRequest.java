package com.skateshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CategoryPostRequest {

    @NotBlank(message = "the field 'name' is required")
    private String categoryName;
}
