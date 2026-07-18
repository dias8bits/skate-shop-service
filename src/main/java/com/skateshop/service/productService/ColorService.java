package com.skateshop.service.productService;

import com.skateshop.domain.product.Color;
import com.skateshop.exception.BusinessException;
import com.skateshop.exception.NotFoundException;
import com.skateshop.repository.productRepository.ColorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ColorService {

    private final ColorRepository colorRepository;

    public List<Color> findAll(String name) {
        return name == null ? colorRepository.findAll() : Collections.singletonList(findColorByNameOrElseThrow(name));
    }

    public Color findColorByNameOrElseThrow(String name) {
        return colorRepository.findColorByColorNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("color not found"));
    }

    public Color findColorByIdOrElseThrow(Long id) {
        return colorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("color not found"));
    }

    public Color save(Color color) {
        if (colorRepository.findColorByColorNameIgnoreCase(color.getColorName()).isPresent())
            throw new BusinessException(String.format("color '%s' already exists", color.getColorName()));
        return colorRepository.save(color);
    }

    public void delete(Color color) {
        var ColorToDelete = findColorByNameOrElseThrow(color.getColorName());
        colorRepository.delete(ColorToDelete);
    }

}
