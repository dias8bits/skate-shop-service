package com.skateshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SizePostRequest {

    @NotBlank(message = "the field 'sizeName' is required")
    private String sizeName;
}
