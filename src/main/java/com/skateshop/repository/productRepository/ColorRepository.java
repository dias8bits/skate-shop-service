package com.skateshop.repository.productRepository;

import com.skateshop.domain.product.Color;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColorRepository extends JpaRepository<Color, Long> {

    Optional<Color> findColorByColorNameIgnoreCase(String name);
}
