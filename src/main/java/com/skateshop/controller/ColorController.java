package com.skateshop.controller;

import com.skateshop.dto.request.ColorPostRequest;
import com.skateshop.dto.response.ColorGetResponse;
import com.skateshop.dto.response.ColorPostResponse;
import com.skateshop.mapper.ColorMapper;
import com.skateshop.service.productService.ColorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("v1/colors")
@RequiredArgsConstructor
@Log4j2
public class ColorController {

    private final ColorService colorService;
    private final ColorMapper mapper;

    @GetMapping
    public ResponseEntity<List<ColorGetResponse>> findAllOrByName(@RequestParam(required = false) String name) {
        log.debug("request to list all colors, param name: '{}'", name);

        var colors = colorService.findAll(name);

        var colorGetResponse = mapper.toColorGetResponseList(colors);

        return ResponseEntity.ok().body(colorGetResponse);
    }

    @PostMapping
    public ResponseEntity<ColorPostResponse> save(@Valid @RequestBody ColorPostRequest request) {
        log.debug("request to save color with name '{}'", request.getColorName());

        var colorToSave = mapper.toColor(request);

        var colorSaved = colorService.save(colorToSave);

        var colorPostResponse = mapper.toColorPostResponse(colorSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(colorPostResponse);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.debug("request to delete color with id: '{}'", id);

        var colorToDelete = colorService.findColorByIdOrElseThrow(id);

        colorService.delete(colorToDelete);

        return ResponseEntity.noContent().build();
    }

}
