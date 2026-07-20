package com.skateshop.commons;

import com.skateshop.domain.product.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class SizeUtils {

    private final CategoryUtils categoryUtils;

    public List<Size> getSizeList() {
        var category = categoryUtils.getCategoryList().getFirst();

        var size1 = Size.builder()
                .id(1L)
                .sizeName("shape")
                .category(category)
                .build();

        var size2 = Size.builder()
                .id(2L)
                .sizeName("wheel")
                .category(category)
                .build();

        return List.of(size1, size2);
    }

    public Size newSize() {
        var category = categoryUtils.getCategoryList().getLast();
        return Size.builder()
                .id(3L)
                .sizeName("bearing")
                .category(category)
                .build();
    }
}
