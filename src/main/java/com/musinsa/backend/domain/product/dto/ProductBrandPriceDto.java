package com.musinsa.backend.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.musinsa.backend.global.utils.StringFormatUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by kimkh
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "상품 정보")
public class ProductBrandPriceDto {
	@JsonProperty("브랜드")
	@Schema(title = "브랜드", example = "A")
	private String brand;
	@JsonProperty("가격")
	@Schema(title = "상품 가격", example = "1,000")
	private String price;

	@Builder
	public ProductBrandPriceDto(String brand, Long price) {
		this.brand = brand;
		this.price = StringFormatUtils.setPriceComma(price.toString());
	}
}
