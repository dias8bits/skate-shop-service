package com.skateshop.service.productService;

import com.skateshop.domain.product.Brand;
import com.skateshop.exception.BusinessException;
import com.skateshop.exception.NotFoundException;
import com.skateshop.repository.productRepository.BrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;

    public List<Brand> findAll(String name) {
        return name == null ? brandRepository.findAll() : Collections.singletonList(findBrandByNameOrElseThrow(name));
    }

    public Brand findBrandByNameOrElseThrow(String name) {
        return brandRepository.findBrandByBrandNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("brand not found"));
    }

    public Brand findBrandByIdOrElseThrow(Long id) {
        return brandRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("brand not found"));
    }

    public Brand save(Brand brand) {
        if (brandRepository.findBrandByBrandNameIgnoreCase(brand.getBrandName()).isPresent())
            throw new BusinessException(String.format("brand '%s' already exists", brand.getBrandName()));
        return brandRepository.save(brand);
    }

    public void delete(Brand brand) {
        var brandToDelete = findBrandByNameOrElseThrow(brand.getBrandName());
        brandRepository.delete(brandToDelete);
    }
}
