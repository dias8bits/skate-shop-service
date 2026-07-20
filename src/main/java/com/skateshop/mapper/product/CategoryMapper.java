package com.skateshop.mapper.product;

import com.skateshop.domain.product.Category;
import com.skateshop.dto.request.CategoryPostRequest;
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
