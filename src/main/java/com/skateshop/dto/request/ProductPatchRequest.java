package com.skateshop.dto.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class ProductPatchRequest {

    private String name;

    private BigDecimal price;

    private Long sizeId;

    private Long brandId;

    private Long colorId;

    private Long categoryId;

    private String description;
}
