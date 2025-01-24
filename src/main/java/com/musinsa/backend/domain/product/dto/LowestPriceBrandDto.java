package com.musinsa.backend.domain.product.dto;

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
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(title = "브랜드 최저가 정보")
public class LowestPriceBrandDto {
	@Schema(title = "최저가 정보")
	@JsonProperty("최저가")
	private LowestPriceBrandCategoryDto lowestPriceBrandCategoryDto;
}
