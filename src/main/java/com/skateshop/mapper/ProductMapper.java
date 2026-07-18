package com.skateshop.mapper;

import com.skateshop.domain.product.Product;
import com.skateshop.dto.request.ProductPatchRequest;
import com.skateshop.dto.request.ProductPostRequest;
import com.skateshop.dto.request.ProductPutRequest;
import com.skateshop.dto.response.ProductGetResponse;
import com.skateshop.dto.response.ProductPatchResponse;
import com.skateshop.dto.response.ProductPostResponse;
import com.skateshop.dto.response.ProductPutResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {

    @Mapping(target = "size", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "color", ignore = true)
    Product toProduct(ProductPostRequest request);

    @Mapping(target = "size", source = "size.sizeName")
    @Mapping(target = "category", source = "category.categoryName")
    @Mapping(target = "brand", source = "brand.brandName")
    @Mapping(target = "color", source = "color.colorName")
    ProductPostResponse toProductPostResponse(Product product);

    @Mapping(target = "size", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "color", ignore = true)
    Product toProduct(ProductPutRequest request);

    @Mapping(target = "size", source = "size.sizeName")
    @Mapping(target = "category", source = "category.categoryName")
    @Mapping(target = "brand", source = "brand.brandName")
    @Mapping(target = "color", source = "color.colorName")
    ProductPutResponse toProductPutResponse(Product product);

    @Mapping(target = "size", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "color", ignore = true)
    Product toProduct(ProductPatchRequest request);

    @Mapping(target = "size", source = "size.sizeName")
    @Mapping(target = "category", source = "category.categoryName")
    @Mapping(target = "brand", source = "brand.brandName")
    @Mapping(target = "color", source = "color.colorName")
    ProductPatchResponse toProductPatchResponse(Product product);

    @Mapping(target = "size", source = "size.sizeName")
    @Mapping(target = "category", source = "category.categoryName")
    @Mapping(target = "brand", source = "brand.brandName")
    @Mapping(target = "color", source = "color.colorName")
    ProductGetResponse toProductGetResponse(Product product);

    List<ProductGetResponse> toProductGetResponseList(List<Product> products);

}
