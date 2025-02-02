package com.musinsa.backend.domain.product.dto;

import java.io.Serial;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.musinsa.backend.global.utils.StringFormatUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by kimkh
 */

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "카테고리 가격 정보")
public class LowestPriceCategoryDto implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
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
