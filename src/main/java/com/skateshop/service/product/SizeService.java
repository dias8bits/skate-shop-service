package com.skateshop.service.product;

import com.skateshop.domain.product.Size;
import com.skateshop.exception.BusinessException;
import com.skateshop.exception.NotFoundException;
import com.skateshop.repository.product.SizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SizeService {

    private final SizeRepository sizeRepository;
    private final CategoryService categoryService;

    public List<Size> findAll(Long id) {
        return id == null ? sizeRepository.findAll() : sizeRepository.findAllByCategory_Id(id);
    }

    public Size findSizeByNameOrElseThrow(String name) {
        return sizeRepository.findSizeBySizeNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("size not found"));
    }

    public Size findSizeByIdOrElseThrow(Long id) {
        return sizeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("size not found"));
    }

    public Size save(Size size, String categoryName) {
        var categoryToSave = categoryService.findCategoryByNameOrElseThrow(categoryName);

        if (sizeRepository.existsByCategory_IdAndSizeNameIgnoreCase(categoryToSave.getId(), size.getSizeName())) {
            throw new BusinessException(String
                    .format("size '%s' already exists for '%s' category", size.getSizeName(), categoryName));
        }

        size.setCategory(categoryToSave);
        return sizeRepository.save(size);
    }

    public void delete(Size size) {
        var SizeToDelete = findSizeByNameOrElseThrow(size.getSizeName());
        sizeRepository.delete(SizeToDelete);
    }

}
