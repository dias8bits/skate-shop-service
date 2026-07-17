package com.skateshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class BrandPostRequest {

    @NotBlank(message = "the field 'brandName' is required")
    private String brandName;
}
