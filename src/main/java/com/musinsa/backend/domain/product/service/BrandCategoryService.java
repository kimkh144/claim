package com.musinsa.backend.domain.product.service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.View;

import com.musinsa.backend.domain.product.repository.BrandCategoryRepository;
import com.musinsa.backend.global.enums.ErrorCode;
import com.musinsa.backend.global.common.exception.ServiceException;
import com.musinsa.backend.global.utils.CategoryUtils;
import com.musinsa.backend.domain.product.dto.request.RequestBrandCategoryDto;
import com.musinsa.backend.domain.product.dto.ProductPriceDto;
import com.musinsa.backend.domain.product.dto.BrandLowestPriceResultDto;
import com.musinsa.backend.domain.product.dto.LowestPriceBrandDto;
import com.musinsa.backend.domain.product.dto.LowestPriceBrandCategoryDto;
import com.musinsa.backend.domain.product.dto.LowestPriceCategoryDto;
import com.musinsa.backend.domain.product.dto.CategoryPriceLowestAndHighestDto;
import com.musinsa.backend.domain.product.entity.BrandCategoryEntity;
import com.musinsa.backend.domain.product.entity.BrandCategoryId;

import lombok.RequiredArgsConstructor;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Service
@RequiredArgsConstructor
public class BrandCategoryService {
    private final BrandCategoryRepository brandCategoryRepository;
    private final View error;

    /* 브랜드 상품 등록 */
    @Transactional
    public void createBrand(RequestBrandCategoryDto requestBrandCategoryDto) {
        /* 브랜드 카테고리 조회 및 유효성 검사 */
        Optional<BrandCategoryEntity> brandCategoryEntity = brandCategoryRepository.findByIdBrandAndIdCategory(
            requestBrandCategoryDto.getBrand(), requestBrandCategoryDto.getCategory());
        if (brandCategoryEntity.isPresent()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.VL00005, "동일한 브랜드, 카테고리 정보가 존재 합니다.");
        }
        /* 브랜드, 카테고리, 가격 정보 생성 */
        BrandCategoryEntity createBrandCategoryEntity = BrandCategoryEntity.builder()
            .id(BrandCategoryId.builder()
                .brand(requestBrandCategoryDto.getBrand())
                .category(requestBrandCategoryDto.getCategory())
                .build())
            .price(requestBrandCategoryDto.getPrice())
            .build();
        brandCategoryRepository.save(createBrandCategoryEntity);
    }

    /* 브랜드 상품 수정 */
    @Transactional
    public void updateBrand(RequestBrandCategoryDto requestBrandCategoryDto) {
        /* 브랜드, 카테고리 조회 */
        BrandCategoryEntity brandCategoryEntity = getBrandCategory(requestBrandCategoryDto);
        /* 브랜드, 카테고리, 가격 수정 */
        BrandCategoryEntity updateBrandCategoryEntity = brandCategoryEntity.toBuilder()
            .price(requestBrandCategoryDto.getPrice())
            .build();
        brandCategoryRepository.save(updateBrandCategoryEntity);
    }

    /* 브랜드 상품 삭제 */
    @Transactional
    public void deleteBrand(RequestBrandCategoryDto requestBrandCategoryDto) {
        /* 브랜드, 카테고리 조회 */
        BrandCategoryEntity brandCategoryEntity = getBrandCategory(requestBrandCategoryDto);
        /* 브랜드, 카테고리 삭제 */
        brandCategoryRepository.delete(brandCategoryEntity);
    }

    /* 카테고리 별 최저 가격 브랜드와 상품 가격, 총액을 조회  */
    public BrandLowestPriceResultDto getLowestPriceProductsByCategory() {
        /* 카테고리 별 최저가 브랜드 조회 */
        List<BrandCategoryEntity> brandCategoryEntities = brandCategoryRepository.findLowestPriceProductsByCategory();
        /* 상품 카테고리 가격 총액 계산 */
        Long totalPrice = brandCategoryEntities.stream()
            .mapToLong(BrandCategoryEntity::getPrice)
            .sum();
        /* 카테고리 정렬 및 DTO 매핑 */
        List<ProductPriceDto> brandLowestPriceDtos = brandCategoryEntities.stream()
            .map(brandCategoryEntity -> new ProductPriceDto(brandCategoryEntity.getId().getCategory(),
                brandCategoryEntity.getId().getBrand(), brandCategoryEntity.getPrice()))
            .sorted(Comparator.comparing(dto -> CategoryUtils.VALID_CATEGORIES.indexOf(dto.getCategory())))
            .toList();
        /* 응답 DTO 생성 */
        return new BrandLowestPriceResultDto(brandLowestPriceDtos, totalPrice);
    }


    /* 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회 */

	@Cacheable(cacheNames = "lowestPriceProductsByBrand")
    public LowestPriceBrandDto getLowestPriceProductsByBrand() {
        /* 최저가 단일 브랜드 정보 조회 */
        List<BrandCategoryEntity> brandCategoryEntities = brandCategoryRepository.findLowestPriceBrand();
        /* 단일 브랜드 가격 총액 계산 */
        Long totalPrice = brandCategoryEntities.stream()
            .mapToLong(BrandCategoryEntity::getPrice)
            .sum();
        /* 카테고리 정렬 및 DTO 매핑 */
        List<LowestPriceCategoryDto> brandLowestPriceDtos = brandCategoryEntities.stream()
            .map(brandCategoryEntity -> new LowestPriceCategoryDto(brandCategoryEntity.getId().getCategory(),
                brandCategoryEntity.getPrice()))
            .sorted(Comparator.comparing(dto -> CategoryUtils.VALID_CATEGORIES.indexOf(dto.getCategory())))
            .toList();
        /* 응답 DTO 생성 */
        return LowestPriceBrandDto.builder()
            .lowestPriceBrandCategoryDto(LowestPriceBrandCategoryDto.builder()
                .brand(brandCategoryEntities.get(0).getId().getBrand())
                .categories(brandLowestPriceDtos)
                .totalPrice(totalPrice)
                .build())
            .build();
    }

    /* 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회 */
    public CategoryPriceLowestAndHighestDto getCategoryPriceLowestAndHighest(String categoryName) {
        /* 유효성 검증 */
        if (!CategoryUtils.isValidCategory(categoryName)) {
            throw new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.VL00005, "유효하지 않은 카테고리 명 입니다.");
        }
        /* 카테고리 최저가, 최고가 조회 */
        return brandCategoryRepository.findCategoryPriceLowestAndHighest(categoryName);
    }

    /* 브랜드, 카테고리 조회 */
    private BrandCategoryEntity getBrandCategory(RequestBrandCategoryDto requestBrandCategoryDto) {
        /* 브랜드 카테고리 조회 */
        return brandCategoryRepository.findByIdBrandAndIdCategory(
                requestBrandCategoryDto.getBrand(), requestBrandCategoryDto.getCategory())
            .orElseThrow(() -> new ServiceException(HttpStatus.BAD_REQUEST.value(), ErrorCode.VL00005,
                "브랜드, 카테고리 정보가 존재하지 않습니다."));
    }
}
