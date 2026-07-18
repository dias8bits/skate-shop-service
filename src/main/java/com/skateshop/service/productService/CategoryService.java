package com.skateshop.service.productService;

import com.skateshop.domain.product.Category;
import com.skateshop.exception.BusinessException;
import com.skateshop.exception.NotFoundException;
import com.skateshop.repository.productRepository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll(String name) {
        return name == null ? categoryRepository.findAll() : Collections.singletonList(findCategoryByNameOrElseThrow(name));
    }

    public Category findCategoryByNameOrElseThrow(String name) {
        return categoryRepository.findCategoryByCategoryNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("category not found"));
    }

    public Category findCategoryByIdOrElseThrow(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("category not found"));
    }

    public Category save(Category category) {
        if (categoryRepository.findCategoryByCategoryNameIgnoreCase(category.getCategoryName()).isPresent())
            throw new BusinessException(String.format("category '%s' already exists", category.getCategoryName()));
        return categoryRepository.save(category);
    }

    public void delete(Category category) {
        var CategoryToDelete = findCategoryByNameOrElseThrow(category.getCategoryName());
        categoryRepository.delete(CategoryToDelete);
    }

}
