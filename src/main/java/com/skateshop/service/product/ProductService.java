package com.skateshop.service.product;

import com.skateshop.domain.product.Product;
import com.skateshop.exception.NotFoundException;
import com.skateshop.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandService brandService;
    private final ColorService colorService;
    private final SizeService sizeService;
    private final CategoryService categoryService;

    public List<Product> findAll(UUID id) {
        return id == null ? productRepository.findAll() : Collections.singletonList(findProductByIdOrElseThrow(id));
    }

    public Product findProductByNameOrElseThrow(String name) {
        return productRepository.findProductByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException(
                        String.format("product with name %s not found", name)
                ));
    }

    public Product findProductByIdOrElseThrow(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("product not found"));
    }

    @Transactional
    public Product save(Product productToSave, Long sizeId, Long brandId, Long colorId, Long categoryId) {
        resolveProductRelations(productToSave, sizeId, brandId, colorId, categoryId);
        return productRepository.save(productToSave);
    }

    @Transactional
    public Product update(Product product, UUID id, Long sizeId, Long brandId, Long colorId, Long categoryId) {
        var productToUpdate = findProductByIdOrElseThrow(id);

        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setName(product.getName());
        productToUpdate.setDescription(product.getDescription());

        resolveProductRelations(productToUpdate, sizeId, brandId, colorId, categoryId);

        return productRepository.save(productToUpdate);
    }

    @Transactional
    public Product updateSelectedFields(Product product, UUID id) {
        var productToUpdate = findProductByIdOrElseThrow(id);

        if (product.getName() != null)
            productToUpdate.setName(product.getName());

        if (product.getPrice() != null)
            productToUpdate.setPrice(product.getPrice());

        if (product.getDescription() != null)
            productToUpdate.setDescription(product.getDescription());

        if (product.getSize() != null)
            productToUpdate.setSize(product.getSize());

        if (product.getBrand() != null)
            productToUpdate.setBrand(product.getBrand());

        if (product.getColor() != null)
            productToUpdate.setColor(product.getColor());

        if (product.getBrand() != null)
            productToUpdate.setCategory(product.getCategory());

        return productRepository.save(productToUpdate);
    }

    @Transactional
    public void delete(Product product) {
        var productToDelete = findProductByIdOrElseThrow(product.getId());
        productRepository.delete(productToDelete);
    }

    private Product resolveProductRelations(Product product, Long sizeId, Long brandId, Long colorId, Long categoryId) {
        product.setSize(sizeService.findSizeByIdOrElseThrow(sizeId));
        product.setBrand(brandService.findBrandByIdOrElseThrow(brandId));
        product.setColor(colorService.findColorByIdOrElseThrow(colorId));
        product.setCategory(categoryService.findCategoryByIdOrElseThrow(categoryId));
        return product;
    }
}
