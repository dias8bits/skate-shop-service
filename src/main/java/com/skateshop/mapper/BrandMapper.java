package com.skateshop.mapper;

import com.skateshop.domain.product.Brand;
import com.skateshop.dto.request.BrandPostRequest;
import com.skateshop.dto.response.BrandGetResponse;
import com.skateshop.dto.response.BrandPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BrandMapper {

    Brand toBrand(BrandPostRequest request);

    BrandPostResponse toBrandPostResponse(Brand brand);

    List<BrandGetResponse> toBrandGetResponseList(List<Brand> brands);
}
