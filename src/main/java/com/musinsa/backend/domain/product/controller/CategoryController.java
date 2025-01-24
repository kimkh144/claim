package com.musinsa.backend.domain.product.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musinsa.backend.global.common.response.BasicResponseDTO;
import com.musinsa.backend.global.constants.swagger.SwaggerConstants;
import com.musinsa.backend.domain.product.dto.BrandLowestPriceResultDto;
import com.musinsa.backend.domain.product.dto.CategoryPriceLowestAndHighestDto;
import com.musinsa.backend.domain.product.service.BrandCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Tag(name = SwaggerConstants.Category.CATEGORY_TAG)
@RequestMapping(value = SwaggerConstants.Category.CATEGORY_V1)
@RequiredArgsConstructor
@RestController
public class CategoryController {
    private final BrandCategoryService brandCategoryService;

    /**
     * 구현 1)- 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API
     */
    @Operation(summary = SwaggerConstants.Category.CATEGORY_API_V1_DESC, description =
        SwaggerConstants.Category.CATEGORY_API_V1_VERSION
            + SwaggerConstants.Category.CATEGORY_API_V1_DESC, responses = {
        @ApiResponse(responseCode = SwaggerConstants.HTTP_STATUS_OK, description = SwaggerConstants.Category.CATEGORY_API_V1_DESC, content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = BrandLowestPriceResultDto.class))
        })
    })
    @GetMapping(value = SwaggerConstants.Category.CATEGORY_API_V1_URL)
    public Object category() {
        return BasicResponseDTO.builder()
            .data(brandCategoryService.getLowestPriceProductsByCategory())
            .build();
    }

    /**
     * 구현 3) - 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API
     */
    @Operation(summary = SwaggerConstants.Category.CATEGORY_API_V3_DESC, description =
        SwaggerConstants.Category.CATEGORY_API_V3_VERSION
            + SwaggerConstants.Category.CATEGORY_API_V3_DESC, responses = {
        @ApiResponse(responseCode = SwaggerConstants.HTTP_STATUS_OK, description =
            SwaggerConstants.Category.CATEGORY_API_V3_VERSION
                + SwaggerConstants.Category.CATEGORY_API_V3_DESC, content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = CategoryPriceLowestAndHighestDto.class))
        })
    })

    @GetMapping(value = SwaggerConstants.Category.CATEGORY_API_V3_URL)
    public Object getCategory(
        @Parameter(description = "카테고리", in = ParameterIn.PATH, example = "상의")
        @PathVariable(name = "category") String category
    ) {
        return BasicResponseDTO.builder()
            .data(brandCategoryService.getCategoryPriceLowestAndHighest(category))
            .build();
    }
}
