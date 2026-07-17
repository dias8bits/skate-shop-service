package com.skateshop.mapper;

import com.skateshop.domain.product.Brand;
import com.skateshop.domain.product.Category;
import com.skateshop.dto.request.BrandPostRequest;
import com.skateshop.dto.request.CategoryPostRequest;
import com.skateshop.dto.response.BrandGetResponse;
import com.skateshop.dto.response.BrandPostResponse;
import com.skateshop.dto.response.CategoryGetResponse;
import com.skateshop.dto.response.CategoryPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryMapper {

    Category toCategory(CategoryPostRequest request);

    CategoryPostResponse toCategoryPostResponse(Category category);

    List<CategoryGetResponse> toCategoryGetResponseList(List<Category> categories);
}
