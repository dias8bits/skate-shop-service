package com.skateshop.controller.product;

import com.skateshop.dto.request.BrandPostRequest;
import com.skateshop.dto.response.BrandGetResponse;
import com.skateshop.dto.response.BrandPostResponse;
import com.skateshop.mapper.product.BrandMapper;
import com.skateshop.service.product.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/brands")
@RequiredArgsConstructor
@Log4j2
public class BrandController {

    private final BrandService brandService;
    private final BrandMapper mapper;

    @GetMapping
    public ResponseEntity<List<BrandGetResponse>> findAllOrByName(@RequestParam(required = false) String name) {
        log.debug("request to list all brands, param name: '{}'", name);

        var brands = brandService.findAll(name);

        var brandGetResponse = mapper.toBrandGetResponseList(brands);

        return ResponseEntity.ok().body(brandGetResponse);
    }

    @PostMapping
    public ResponseEntity<BrandPostResponse> save(@Valid @RequestBody BrandPostRequest request) {
        log.debug("request to save brand with name '{}'", request.getBrandName());

        var brandToSave = mapper.toBrand(request);

        var brandSaved = brandService.save(brandToSave);

        var brandPostResponse = mapper.toBrandPostResponse(brandSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(brandPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("request to delete brand with id: '{}'", id);

        var brandToDelete = brandService.findBrandByIdOrElseThrow(id);

        brandService.delete(brandToDelete);

        return ResponseEntity.noContent().build();
    }

}
