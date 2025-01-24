package com.musinsa.backend.domain.product.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by kimkh on 10/30/24.
 */

@Getter
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "카테고리 가격 정보")
public class CategoryPriceLowestAndHighestDto {
	@Schema(title = "카테고리", example = "상의")
	@JsonProperty("카테고리")
	private String category;
	@Schema(title = "최저가 정보")
	@JsonProperty("최저가")
	private List<ProductBrandPriceDto> lowerPrice;
	@Schema(title = "최고가 정보")
	@JsonProperty("최고가")
	private List<ProductBrandPriceDto> highPrice;
}
