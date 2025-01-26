package com.musinsa.backend.domain.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.musinsa.backend.global.common.response.BasicResponseDTO;
import com.musinsa.backend.global.constants.swagger.SwaggerConstants;
import com.musinsa.backend.domain.product.dto.request.RequestBrandCategoryDto;
import com.musinsa.backend.domain.product.dto.LowestPriceBrandDto;
import com.musinsa.backend.domain.product.service.BrandCategoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Created by kimkh
 */
@Tag(name = SwaggerConstants.Brand.BRAND_TAG)
@RequestMapping(value = SwaggerConstants.Brand.BRAND_V1)
@RequiredArgsConstructor
@RestController
public class BrandController {
    private final BrandCategoryService brandCategoryService;

    /**
     * 구현 2) - 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API
     */
    @Operation(summary = SwaggerConstants.Brand.BRAND_API_V1_DESC, description =
        SwaggerConstants.Brand.BRAND_API_V1_VERSION + SwaggerConstants.Brand.BRAND_API_V1_DESC, responses = {
        @ApiResponse(responseCode = SwaggerConstants.HTTP_STATUS_OK, description = SwaggerConstants.Brand.BRAND_API_V1_DESC, content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = LowestPriceBrandDto.class))
        })
    })
    @GetMapping(value = SwaggerConstants.Brand.BRAND_API_V1_URL)
    public BasicResponseDTO<?> brands() {
        return BasicResponseDTO.builder()
            .data(brandCategoryService.getLowestPriceProductsByBrand())
            .build();
    }

    /**
     * 구현 4) 브랜드 및 상품을 추가 API
     */
    @Operation(summary = SwaggerConstants.Brand.BRAND_API_V4_CREATE_DESC, description =
        SwaggerConstants.Brand.BRAND_API_V4_VERSION + SwaggerConstants.Brand.BRAND_API_V4_CREATE_DESC, responses = {
        @ApiResponse(responseCode = SwaggerConstants.HTTP_STATUS_OK, description =
            SwaggerConstants.Brand.BRAND_API_V4_VERSION + SwaggerConstants.Brand.BRAND_API_V4_CREATE_DESC, content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RequestBrandCategoryDto.class))
        })
    })

    @PostMapping(value =  SwaggerConstants.Brand.BRAND_API_V4_URL)
    public ResponseEntity<BasicResponseDTO> createCategory(
        @RequestBody @Valid RequestBrandCategoryDto requestBrandCategoryDto
    ) {
        brandCategoryService.createBrand(requestBrandCategoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponseDTO.builder()
            .data(requestBrandCategoryDto)
            .build());
    }

    /**
     * 구현 4) 브랜드 및 상품을 업데이트 API
     */
    @Operation(summary = SwaggerConstants.Brand.BRAND_API_V4_UPDATE_DESC, description =
        SwaggerConstants.Brand.BRAND_API_V4_VERSION + SwaggerConstants.Brand.BRAND_API_V4_UPDATE_DESC, responses = {
        @ApiResponse(responseCode = SwaggerConstants.HTTP_STATUS_OK, description =
            SwaggerConstants.Brand.BRAND_API_V4_VERSION + SwaggerConstants.Brand.BRAND_API_V4_UPDATE_DESC, content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = RequestBrandCategoryDto.class))
        })
    })

    @PutMapping(value = SwaggerConstants.Brand.BRAND_API_V4_URL)
    public BasicResponseDTO<?> updateCategory(
        @RequestBody @Valid RequestBrandCategoryDto requestBrandCategoryDto
    ) {
        brandCategoryService.updateBrand(requestBrandCategoryDto);
        return BasicResponseDTO.builder()
            .data(requestBrandCategoryDto)
            .build();
    }

    /**
     * 구현 4) 브랜드 및 상품을 삭제 API
     */
    @Operation(summary = SwaggerConstants.Brand.BRAND_API_V4_DELETE_DESC, description =
        SwaggerConstants.Brand.BRAND_API_V4_VERSION + SwaggerConstants.Brand.BRAND_API_V4_CREATE_DESC, responses = {
        @ApiResponse(responseCode = SwaggerConstants.HTTP_STATUS_OK, description =
            SwaggerConstants.Brand.BRAND_API_V4_VERSION + SwaggerConstants.Brand.BRAND_API_V4_DELETE_DESC)
    })

    @DeleteMapping(value = SwaggerConstants.Brand.BRAND_API_V4_URL)
    public ResponseEntity<BasicResponseDTO> deleteCategory(
        @RequestBody @Valid RequestBrandCategoryDto requestBrandCategoryDto
    ) {
        brandCategoryService.deleteBrand(requestBrandCategoryDto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
