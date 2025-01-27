package com.musinsa.backend.domain.product.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musinsa.backend.domain.product.dto.LowestPriceBrandCategoryDto;
import com.musinsa.backend.domain.product.dto.LowestPriceBrandDto;
import com.musinsa.backend.domain.product.dto.request.RequestBrandCategoryDto;
import com.musinsa.backend.domain.product.entity.BrandCategoryEntity;
import com.musinsa.backend.domain.product.repository.BrandCategoryRepository;

/**
 * Created by kimkh on 2025. 1. 26..
 */
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrandCategoryRepository brandCategoryRepository;

    @Test
    @Order(1)
    @DisplayName("GET /v1/products/brands/lowest-price - 성공")
    void brandsSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1/products/brands/lowest-price")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();

        LowestPriceBrandDto lowestPriceBrandDto = new ObjectMapper().treeToValue(
            new ObjectMapper().readTree(mvcResult.getResponse().getContentAsString()).get("data"),
            LowestPriceBrandDto.class);

        // 최저가 정보 존재 여부
        assertNotNull(lowestPriceBrandDto.getLowestPriceBrandCategoryDto(), "최저가 정보가 존재하지 않습니다.");

        LowestPriceBrandCategoryDto categoryDto = lowestPriceBrandDto.getLowestPriceBrandCategoryDto();

        assertAll(
            "최저가 브랜드 상세 정보 검증",
            () -> assertNotNull(categoryDto.getBrand(), "최저가 브랜드 정보가 존재하지 않습니다."),
            () -> assertNotNull(categoryDto.getCategories(), "최저가 카테고리 정보가 존재하지 않습니다."),
            () -> assertNotNull(categoryDto.getTotalPrice(), "최저가 총액 정보가 존재하지 않습니다."),
            () -> assertEquals("D", categoryDto.getBrand(), "최저가 브랜드 정보가 예상과 다릅니다."),
            () -> assertEquals("36,100", categoryDto.getTotalPrice(), "최저가 총액 정보가 예상과 다릅니다."),
            () -> assertEquals(8, categoryDto.getCategories().size(), "최저가 카테고리 갯수가 예상과 다릅니다.")
        );
    }

    @Test
    @Order(2)
    @DisplayName("POST /v1/products/brands - 성공")
    void createCategorySuccess() throws Exception {

        deleteCategorySuccess();
        RequestBrandCategoryDto requestBrandCategoryDto = RequestBrandCategoryDto.builder()
            .brand("A")
            .category("상의")
            .price(1000L)
            .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/products/brands")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestBrandCategoryDto))
            )
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();

        Optional<BrandCategoryEntity> brandCategoryEntity = brandCategoryRepository.findByIdBrandAndIdCategory("A", "상의");
        assertTrue(brandCategoryEntity.isPresent(), "브랜드/카테고리 정보가 존재 하지 않습니다.");
        assertEquals(requestBrandCategoryDto.getPrice(), brandCategoryEntity.get().getPrice(), "브랜드/카테고리 가격 정보가 예상과 다릅니다.");

    }

    @Test
    @Order(3)
    @DisplayName("PUT /v1/products/brands - 성공")
    void updateCategorySuccess() throws Exception{
        RequestBrandCategoryDto requestBrandCategoryDto = RequestBrandCategoryDto.builder()
            .brand("A")
            .category("상의")
            .price(1001L)
            .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/products/brands")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestBrandCategoryDto))
            )
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();

        Optional<BrandCategoryEntity> brandCategoryEntity = brandCategoryRepository.findByIdBrandAndIdCategory("A", "상의");
        // 존재 여부를 먼저 검증
        assertTrue(brandCategoryEntity.isPresent(), "브랜드/카테고리 정보가 존재하지 않습니다.");

        // Optional 값이 존재할 경우 상세 정보 검증
        brandCategoryEntity.ifPresent(entity -> assertAll(
            "브랜드/카테고리 생성 정보 상세 검증",
            () -> assertEquals(requestBrandCategoryDto.getBrand(), entity.getId().getBrand(), "브랜드 정보가 예상과 다릅니다."),
            () -> assertEquals(requestBrandCategoryDto.getCategory(), entity.getId().getCategory(), "카테고리 정보가 예상과 다릅니다."),
            () -> assertEquals(requestBrandCategoryDto.getPrice(), entity.getPrice(), "가격 정보가 예상과 다릅니다.")
        ));

    }

    @Test
    @Order(4)
    @DisplayName("DELETE /v1/products/brands - 성공")
    void deleteCategorySuccess() throws Exception {
        RequestBrandCategoryDto requestBrandCategoryDto = RequestBrandCategoryDto.builder()
            .brand("A")
            .category("상의")
            .price(1000L)
            .build();

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/products/brands")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestBrandCategoryDto))
            )
            .andExpect(MockMvcResultMatchers.status().isNoContent())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();

        Optional<BrandCategoryEntity> brandCategoryEntity = brandCategoryRepository.findByIdBrandAndIdCategory("A",
            "상의");
        /* 삭제 후 미 존재 여부 확인 */
        assertFalse(brandCategoryEntity.isPresent(), "브랜드/카테고리 정보가 존재 합니다.");

    }
}