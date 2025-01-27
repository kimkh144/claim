package com.musinsa.backend.domain.product.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.musinsa.backend.global.utils.StringFormatUtils;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(title = "상품 리스트 정보")
public class BrandLowestPriceResultDto {
	@Schema(title = "상품 리스트")
	private List<ProductPriceDto> products;
	@Schema(title = "상품 가격 총액", example = "1,000")
	private String totalPrice;

	public BrandLowestPriceResultDto(List<ProductPriceDto> products, Long totalPrice) {
		this.products = products;
		this.totalPrice = StringFormatUtils.setPriceComma(totalPrice.toString());
	}
}
