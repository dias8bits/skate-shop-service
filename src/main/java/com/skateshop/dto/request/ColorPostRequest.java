package com.skateshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ColorPostRequest {

    @NotBlank(message = "the field 'colorName' is required")
    private String colorName;
}
