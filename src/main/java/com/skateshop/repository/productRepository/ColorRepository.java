package com.skateshop.repository.productRepository;

import com.skateshop.domain.product.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorRepository extends JpaRepository<Color, Long> {
}
