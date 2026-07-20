package com.skateshop.commons;

import com.skateshop.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductUtils {

    public static final LocalDateTime CREATED_AT_FIXO =
            LocalDateTime.of(2026, 7, 19, 12, 30);

    private final BrandUtils brandUtils;
    private final CategoryUtils categoryUtils;
    private final SizeUtils sizeUtils;
    private final ColorUtils colorUtils;

    public List<Product> getProductList() {
        var brand = brandUtils.getBrandList().getFirst();
        var size = sizeUtils.getSizeList().getFirst();
        var color = colorUtils.getColorList().getFirst();
        var category = categoryUtils.getCategoryList().getFirst();

        var product1 = Product.builder()
                .id(UUID.fromString("09c351be-1482-4741-86e2-e9d8d65ef10f"))
                .name("shape 8")
                .description("shape maple")
                .price(BigDecimal.valueOf(300))
                .color(color)
                .size(size)
                .brand(brand)
                .category(category)
                .createdAt(CREATED_AT_FIXO)
                .build();

        var product2 = Product.builder()
                .id(UUID.fromString("610fc119-3f6d-47bd-9469-a5bc79c5e5c1"))
                .name("shape 8.25")
                .description("shape maple")
                .price(BigDecimal.valueOf(300))
                .color(color)
                .size(size)
                .brand(brand)
                .category(category)
                .createdAt(CREATED_AT_FIXO)
                .build();

        return List.of(product1, product2);
    }

    public Product newProduct() {
        var brand = brandUtils.getBrandList().getFirst();
        var size = sizeUtils.getSizeList().getFirst();
        var color = colorUtils.getColorList().getFirst();
        var category = categoryUtils.getCategoryList().getFirst();

        return Product.builder()
                .id(UUID.fromString("a674069b-fad0-4249-a83a-33dc548c2c52"))
                .name("shape 8.25")
                .description("shape maple")
                .price(BigDecimal.valueOf(300))
                .color(color)
                .size(size)
                .brand(brand)
                .category(category)
                .createdAt(CREATED_AT_FIXO)
                .build();

    }
}