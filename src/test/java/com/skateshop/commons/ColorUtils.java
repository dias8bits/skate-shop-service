package com.skateshop.commons;

import com.skateshop.domain.product.Color;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ColorUtils {

    public List<Color> getColorList() {
        var color1 = Color.builder()
                .id(1L)
                .colorName("black")
                .build();

        var color2 = Color.builder()
                .id(2L)
                .colorName("white")
                .build();

        return List.of(color1, color2);
    }

    public Color newColor() {
        return Color.builder()
                .id(3L)
                .colorName("red")
                .build();
    }
}
