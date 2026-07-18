package com.skateshop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductPostRequest {

    @NotBlank(message = "the field 'name' is required")
    private String name;

    @NotNull(message = "the field 'price' is required")
    private BigDecimal price;

    @NotNull(message = "the field 'sizeId' is required")
    private Long sizeId;

    @NotNull(message = "the field 'brandId' is required")
    private Long brandId;

    @NotNull(message = "the field 'colorId' is required")
    private Long colorId;

    @NotNull(message = "the field 'categoryId' is required")
    private Long categoryId;

    @NotBlank(message = "the field 'description' is required")
    private String description;
}
