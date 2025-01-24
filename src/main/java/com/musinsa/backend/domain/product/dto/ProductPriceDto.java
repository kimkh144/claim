package com.musinsa.backend.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.musinsa.backend.global.utils.StringFormatUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "상품 정보")
public class ProductPriceDto {
	@Schema(title = "브랜드", example = "A")
	private String brand;
	@Schema(title = "카테고리", example = "상의")
	private String category;
	@Schema(title = "상품 가격", example = "1,000")
	private String price;

	@Builder
	public ProductPriceDto(String category, String brand, Long price) {
		this.category = category;
		this.brand = brand;
		this.price = StringFormatUtils.setPriceComma(price.toString());
	}
	@Builder
	public ProductPriceDto(String brand, Long price) {
		this.brand = brand;
		this.price = StringFormatUtils.setPriceComma(price.toString());
	}
}
