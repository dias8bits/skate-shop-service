package com.skateshop.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@JsonPropertyOrder({"id","sizeName", "categoryName"})
public class SizePostResponse {

    private String sizeName;

    private String categoryName;

    private Long id;
}
