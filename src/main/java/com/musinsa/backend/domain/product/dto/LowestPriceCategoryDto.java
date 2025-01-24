package com.musinsa.backend.domain.product.dto;

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
@Schema(title = "카테고리 가격 정보")
public class LowestPriceCategoryDto {
	@Schema(title = "카테고리", example = "상의")
	@JsonProperty("카테고리")
	private String category;
	@Schema(title = "가격", example = "1,000")
	@JsonProperty("가격")
	private String price;

	@Builder
	public LowestPriceCategoryDto(String category, Long price) {
		this.category = category;
		this.price = StringFormatUtils.setPriceComma(price.toString());
	}

}
