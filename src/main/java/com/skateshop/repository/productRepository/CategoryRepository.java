package com.skateshop.repository.productRepository;

import com.skateshop.domain.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findCategoryByCategoryNameIgnoreCase(String name);
}
