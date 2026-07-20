package com.skateshop.mapper.product;

import com.skateshop.domain.product.Size;
import com.skateshop.dto.request.SizePostRequest;
import com.skateshop.dto.response.SizeGetResponse;
import com.skateshop.dto.response.SizePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SizeMapper {

    @Mapping(target = "category", ignore = true)
    Size toSize(SizePostRequest request);

    @Mapping(target = "categoryName", source = "category.categoryName")
    SizePostResponse toSizePostResponse(Size size);

    @Mapping(target = "categoryName", source = "category.categoryName")
    SizeGetResponse toSizeGetResponse(Size size);

    List<SizeGetResponse> toSizeGetResponseList(List<Size> categories);
}
