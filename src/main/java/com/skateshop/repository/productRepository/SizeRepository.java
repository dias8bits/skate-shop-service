package com.skateshop.repository.productRepository;

import com.skateshop.domain.product.Size;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SizeRepository extends JpaRepository<Size, Long> {

    Optional<Size> findSizeBySizeNameIgnoreCase(String name);

    List<Size> findAllByCategory_Id(Long id);

    boolean existsByCategory_IdAndSizeNameIgnoreCase(Long categoryId, String size);
}
