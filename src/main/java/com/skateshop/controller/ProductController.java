package com.skateshop.controller;

import com.skateshop.dto.request.ProductPatchRequest;
import com.skateshop.dto.request.ProductPostRequest;
import com.skateshop.dto.request.ProductPutRequest;
import com.skateshop.dto.response.ProductGetResponse;
import com.skateshop.dto.response.ProductPatchResponse;
import com.skateshop.dto.response.ProductPostResponse;
import com.skateshop.dto.response.ProductPutResponse;
import com.skateshop.mapper.ProductMapper;
import com.skateshop.service.productService.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("v1/products")
public class ProductController {

    private final ProductService productService;

    private final ProductMapper mapper;

    @GetMapping()
    public ResponseEntity<List<ProductGetResponse>> findAllOrById(@RequestParam(required = false) UUID id) {
        log.debug("request to list all products, param id: '{}'", id);

        var products = productService.findAll(id);

        var productGetResponseList = mapper.toProductGetResponseList(products);

        return ResponseEntity.ok(productGetResponseList);
    }

    @GetMapping("{name}")
    public ResponseEntity<ProductGetResponse> findProductByName(@PathVariable String name) {
        log.debug("request to find product with name: '{}'", name);

        var product = productService.findProductByNameOrElseThrow(name);

        var productGetResponse = mapper.toProductGetResponse(product);

        return ResponseEntity.ok(productGetResponse);
    }

    @PostMapping()
    public ResponseEntity<ProductPostResponse> save(@Valid @RequestBody ProductPostRequest request) {
        log.debug("request to save product");

        var productToSave = mapper.toProduct(request);

        var productSaved = productService.save(
                productToSave,
                request.getSizeId(),
                request.getBrandId(),
                request.getColorId(),
                request.getCategoryId()
        );

        var productPostResponse = mapper.toProductPostResponse(productSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(productPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        log.debug("request to delete product with id: '{}'", id);

        var productToDelete = productService.findProductByIdOrElseThrow(id);

        productService.delete(productToDelete);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    public ResponseEntity<ProductPutResponse> update(@Valid @RequestBody ProductPutRequest request,
                                                  @PathVariable UUID id) {
        log.debug("request to update product with id: '{}'", id);

        var productToUpdate = mapper.toProduct(request);

        var productUpdated = productService.update(
                productToUpdate,
                id,
                request.getSizeId(),
                request.getBrandId(),
                request.getColorId(),
                request.getCategoryId()
        );

        var productPutResponse = mapper.toProductPutResponse(productUpdated);

        return ResponseEntity.ok().body(productPutResponse);
    }

    @PatchMapping("{id}")
    public ResponseEntity<ProductPatchResponse> updateSelectedFields(@RequestBody ProductPatchRequest request,
                                                                  @PathVariable UUID id) {
        log.debug("request to updateSelectedFields product with id: '{}'", id);

        var productToUpdate = mapper.toProduct(request);

        var productUpdated = productService.updateSelectedFields(productToUpdate, id);

        var productPutResponse = mapper.toProductPatchResponse(productUpdated);

        return ResponseEntity.ok().body(productPutResponse);
    }

}
