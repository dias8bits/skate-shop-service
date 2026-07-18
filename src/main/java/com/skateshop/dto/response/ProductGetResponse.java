package com.skateshop.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@JsonPropertyOrder({"id", "name", "price", "size", "brand", "color", "category", "description"})
public class ProductGetResponse {

    private UUID id;

    private String name;

    private BigDecimal price;

    private String description;

    private String size;

    private String brand;

    private String color;

    private String category;

    private LocalDateTime createdAt;
}
