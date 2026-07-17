package com.skateshop.mapper;

import com.skateshop.domain.product.Color;
import com.skateshop.dto.request.ColorPostRequest;
import com.skateshop.dto.response.ColorGetResponse;
import com.skateshop.dto.response.ColorPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ColorMapper {

    Color toColor(ColorPostRequest request);

    ColorPostResponse toColorPostResponse(Color color);

    List<ColorGetResponse> toColorGetResponseList(List<Color> colors);
}
