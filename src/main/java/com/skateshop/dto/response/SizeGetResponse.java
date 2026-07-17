package com.skateshop.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonPropertyOrder({"id","sizeName", "categoryName"})
public class SizeGetResponse {

    private Long id;

    private String sizeName;

    private String categoryName;
}
