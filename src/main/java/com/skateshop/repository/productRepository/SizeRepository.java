package com.skateshop.repository.productRepository;

import com.skateshop.domain.product.Size;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SizeRepository extends JpaRepository<Size, Long> {
}
