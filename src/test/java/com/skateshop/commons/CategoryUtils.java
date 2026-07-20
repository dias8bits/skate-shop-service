package com.skateshop.commons;

import com.skateshop.domain.product.Category;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryUtils {

    public List<Category> getCategoryList() {
        var category1 = Category.builder()
                .id(1L)
                .categoryName("shape")
                .build();

        var category2 = Category.builder()
                .id(2L)
                .categoryName("wheel")
                .build();

        return List.of(category1, category2);
    }

    public Category newCategory() {
        return Category.builder()
                .id(3L)
                .categoryName("bearing")
                .build();
    }
}
