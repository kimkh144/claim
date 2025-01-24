package com.musinsa.backend.domain.product.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.musinsa.backend.global.utils.StringFormatUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by kimkh on 10/30/24.
 */

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "상품 최저가 정보")
public class LowestPriceBrandCategoryDto {
	@Schema(title = "브랜드", example = "A")
	@JsonProperty("브랜드")
	private String brand;
	@Schema(title = "카테고리", example = "상의")
	@JsonProperty("카테고리")
	private List<LowestPriceCategoryDto> categories;
	@Schema(title = "총액", example = "1,000")
	@JsonProperty("총액")
	private String totalPrice;

	@Builder
	public LowestPriceBrandCategoryDto(String brand, List<LowestPriceCategoryDto> categories, Long totalPrice) {
		this.brand = brand;
		this.categories = categories;
		this.totalPrice = StringFormatUtils.setPriceComma(totalPrice.toString());
	}
}
