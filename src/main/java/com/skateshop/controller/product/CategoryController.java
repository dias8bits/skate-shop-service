package com.skateshop.controller.product;

import com.skateshop.dto.request.CategoryPostRequest;
import com.skateshop.dto.response.CategoryGetResponse;
import com.skateshop.dto.response.CategoryPostResponse;
import com.skateshop.mapper.product.CategoryMapper;
import com.skateshop.service.product.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/categories")
@RequiredArgsConstructor
@Log4j2
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper mapper;

    @GetMapping
    public ResponseEntity<List<CategoryGetResponse>> findAllOrByName(@RequestParam(required = false) String name) {
        log.debug("request to list all categories, param name: '{}'", name);

        var categories = categoryService.findAll(name);

        var categoryGetResponse = mapper.toCategoryGetResponseList(categories);

        return ResponseEntity.ok().body(categoryGetResponse);
    }

    @PostMapping
    public ResponseEntity<CategoryPostResponse> save(@Valid @RequestBody CategoryPostRequest request) {
        log.debug("request to save category with name '{}'", request.getCategoryName());

        var categoryToSave = mapper.toCategory(request);

        var categorySaved = categoryService.save(categoryToSave);

        var categoryPostResponse = mapper.toCategoryPostResponse(categorySaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("request to delete category with id: '{}'", id);

        var categoryToDelete = categoryService.findCategoryByIdOrElseThrow(id);

        categoryService.delete(categoryToDelete);

        return ResponseEntity.noContent().build();
    }

}
