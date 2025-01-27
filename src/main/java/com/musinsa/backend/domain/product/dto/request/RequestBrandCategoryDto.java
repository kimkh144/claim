package com.musinsa.backend.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by kimkh
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "상품 정보(요청)")
public class RequestBrandCategoryDto {
	@Schema(title = "브랜드", example = "A")
	@NotBlank(message = "브랜드 정보는 필수 입력 되어야 합니다.")
	private String brand;
	@Schema(title = "카테 고리", example = "상의")
	@NotBlank(message = "카테고리 정보는 필수 입력 되어야 합니다.")
	private String category;
	@Schema(title = "상품 가격", example = "1000")
	@Positive(message = "가격은 0원 보다 커야 합니다.")
	private Long price;
}
