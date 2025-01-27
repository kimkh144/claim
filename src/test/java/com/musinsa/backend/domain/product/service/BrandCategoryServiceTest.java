package com.musinsa.backend.domain.product.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.musinsa.backend.domain.product.dto.CategoryPriceLowestAndHighestDto;
import com.musinsa.backend.domain.product.dto.LowestPriceBrandDto;
import com.musinsa.backend.domain.product.dto.ProductBrandPriceDto;
import com.musinsa.backend.domain.product.dto.request.RequestBrandCategoryDto;
import com.musinsa.backend.domain.product.entity.BrandCategoryEntity;
import com.musinsa.backend.domain.product.entity.BrandCategoryId;
import com.musinsa.backend.domain.product.repository.BrandCategoryRepository;
import com.musinsa.backend.global.common.exception.ServiceException;

/**
 * Created by kimkh
 */
public class BrandCategoryServiceTest {

    @InjectMocks
    private BrandCategoryService brandCategoryService;

    @Mock
    private BrandCategoryRepository brandCategoryRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateBrandSuccess() {
        // Arrange
        RequestBrandCategoryDto requestBrandCategoryDto =
            RequestBrandCategoryDto.builder()
                .brand("A")
                .category("상의")
                .price(1000L)
                .build();
        when(brandCategoryRepository.findByIdBrandAndIdCategory("A", "상의")).thenReturn(Optional.empty());

        brandCategoryService.createBrand(requestBrandCategoryDto);
        // Assert
        verify(brandCategoryRepository, times(1)).save(any(BrandCategoryEntity.class));
    }


    @Test
    public void testDeleteBrandSuccess() {
        // Arrange
        RequestBrandCategoryDto requestBrandCategoryDto =
            RequestBrandCategoryDto.builder()
                .brand("A")
                .category("상의")
                .price(1500L)
                .build();
        BrandCategoryEntity existingEntity = BrandCategoryEntity.builder()
            .id(BrandCategoryId.builder().brand("A").category("상의").build())
            .price(100L)
            .build();

        doReturn(Optional.of(existingEntity)).when(brandCategoryRepository)
            .findByIdBrandAndIdCategory("A", "상의");

        // Act
        brandCategoryService.deleteBrand(requestBrandCategoryDto);

        // Assert
        verify(brandCategoryRepository, times(1)).delete(existingEntity);
    }

    @Test
    public void testGetLowestPriceProductsByBrand() {
        // Arrange
        List<BrandCategoryEntity> brandCategoryEntities = new ArrayList<>();

        BrandCategoryEntity entity1 = BrandCategoryEntity.builder()
            .id(BrandCategoryId.builder().brand("A").category("상의").build())
            .price(1000L)
            .build();

        BrandCategoryEntity entity2 = BrandCategoryEntity.builder()
            .id(BrandCategoryId.builder().brand("A").category("바지").build())
            .price(1500L)
            .build();

        brandCategoryEntities.add(entity1);
        brandCategoryEntities.add(entity2);

        // 스텁 설정: 특정 브랜드의 최저 가격 상품 목록 반환
        doReturn(brandCategoryEntities).when(brandCategoryRepository).findLowestPriceBrand();

        // Act: 서비스 메서드 호출
        LowestPriceBrandDto result = brandCategoryService.getLowestPriceProductsByBrand();

        // Assert: 결과 검증
        assertEquals("A", result.getLowestPriceBrandCategoryDto().getBrand());
        assertEquals(2, result.getLowestPriceBrandCategoryDto().getCategories().size());
        assertEquals("2,500", result.getLowestPriceBrandCategoryDto().getTotalPrice()); // 1000 + 1500
        assertEquals("상의", result.getLowestPriceBrandCategoryDto().getCategories().get(0).getCategory());
        assertEquals("1,000", result.getLowestPriceBrandCategoryDto().getCategories().get(0).getPrice());
        assertEquals("바지", result.getLowestPriceBrandCategoryDto().getCategories().get(1).getCategory());
        assertEquals("1,500", result.getLowestPriceBrandCategoryDto().getCategories().get(1).getPrice());

        verify(brandCategoryRepository).findLowestPriceBrand(); // repository 메서드가 호출되었는지 확인
    }

    @Test
    public void testGetCategoryPriceLowestAndHighestSuccess() {
        // Arrange
        String categoryName = "상의";
        CategoryPriceLowestAndHighestDto expectedDto =
            CategoryPriceLowestAndHighestDto.builder()
                .category(categoryName)
                .lowerPrice(List.of(ProductBrandPriceDto.builder().brand("A").price(100L).build()))
                .highPrice(List.of(ProductBrandPriceDto.builder().brand("B").price(300L).build()))
                .build();


        // 스텁 설정: 카테고리의 최저가, 최고가를 반환하도록 설정
        doReturn(expectedDto).when(brandCategoryRepository).findCategoryPriceLowestAndHighest(categoryName);

        // Act: 서비스 메서드 호출
        CategoryPriceLowestAndHighestDto result = brandCategoryService.getCategoryPriceLowestAndHighest(categoryName);

        // Assert: 결과 검증
        assertEquals(expectedDto.getLowerPrice(), result.getLowerPrice());
        assertEquals(expectedDto.getHighPrice(), result.getHighPrice());
    }

    @Test
    public void testGetCategoryPriceLowestAndHighestInvalidCategory() {
        // Arrange
        String invalidCategoryName = "유효하지않은카테고리";

        // Act & Assert: 유효하지 않은 카테고리일 경우 예외가 발생해야 함
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            brandCategoryService.getCategoryPriceLowestAndHighest(invalidCategoryName);
        });

        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getCode());
        assertEquals("유효하지 않은 카테고리 명 입니다.", exception.getMessage());
    }
}
