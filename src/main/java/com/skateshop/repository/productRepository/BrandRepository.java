package com.skateshop.repository.productRepository;

import com.skateshop.domain.product.Brand;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BrandRepository extends JpaRepository<Brand, Long> {
}
