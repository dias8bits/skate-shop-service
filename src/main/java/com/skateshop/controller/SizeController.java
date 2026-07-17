package com.skateshop.controller;

import com.skateshop.dto.request.SizePostRequest;
import com.skateshop.dto.response.SizeGetResponse;
import com.skateshop.dto.response.SizePostResponse;
import com.skateshop.mapper.SizeMapper;
import com.skateshop.service.productService.SizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/sizes")
@RequiredArgsConstructor
@Log4j2
public class SizeController {

    private final SizeService categoryService;
    private final SizeMapper mapper;

    @GetMapping
    public ResponseEntity<List<SizeGetResponse>> findAllOrByCategory(@RequestParam(required = false) Long categoryId) {
        log.debug("request to list all sizes, param id: '{}'", categoryId);

        var categories = categoryService.findAll(categoryId);

        var categoryGetResponse = mapper.toSizeGetResponseList(categories);

        return ResponseEntity.ok().body(categoryGetResponse);
    }

    @PostMapping("{categoryName}")
    public ResponseEntity<SizePostResponse> save(@Valid @RequestBody SizePostRequest request,
                                                 @PathVariable String categoryName) {
        log.debug("request to save category with name '{}'", request.getSizeName());

        var categoryToSave = mapper.toSize(request);

        var categorySaved = categoryService.save(categoryToSave, categoryName);

        var categoryPostResponse = mapper.toSizePostResponse(categorySaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(categoryPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("request to delete category with id: '{}'", id);

        var categoryToDelete = categoryService.findSizeByIdOrElseThrow(id);

        categoryService.delete(categoryToDelete);

        return ResponseEntity.noContent().build();
    }

}
