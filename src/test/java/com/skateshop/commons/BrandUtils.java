package com.skateshop.commons;

import com.skateshop.domain.product.Brand;
import com.skateshop.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class BrandUtils {

    public List<Brand> getBrandList() {
        var brand1 = Brand.builder()
                .id(1L)
                .brandName("red bones")
                .build();

        var brand2 = Brand.builder()
                .id(2L)
                .brandName("baker")
                .build();

        return List.of(brand1, brand2);
    }

    public Brand newBrand() {
        return Brand.builder()
                .id(3L)
                .brandName("blind")
                .build();
    }
}
