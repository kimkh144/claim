package com.musinsa.backend.domain.product.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import com.musinsa.backend.domain.product.dto.LowestPriceBrandDto;
import com.musinsa.backend.domain.product.dto.request.RequestBrandCategoryDto;
import com.musinsa.backend.domain.product.entity.BrandCategoryEntity;
import com.musinsa.backend.domain.product.entity.BrandCategoryId;
import com.musinsa.backend.domain.product.repository.BrandCategoryRepository;

/**
 * Created by kimkh on 2025. 1. 26..
 */
@SpringBootTest
@AutoConfigureMockMvc
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrandCategoryRepository brandCategoryRepository;

    @Test
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

        assertNotNull(lowestPriceBrandDto.getLowestPriceBrandCategoryDto(), "최저가 정보가 존재 하지 않습니다.");
        assertNotNull(lowestPriceBrandDto.getLowestPriceBrandCategoryDto().getBrand(), "최저가 브랜드 정보가 존재 하지 않습니다.");
        assertNotNull(lowestPriceBrandDto.getLowestPriceBrandCategoryDto().getCategories(), "최저가 카테고리 정보가 존재 하지 않습니다.");
        assertNotNull(lowestPriceBrandDto.getLowestPriceBrandCategoryDto().getTotalPrice(), "최저가 총액 정보가 존재 하지 않습니다.");
        assertEquals("D", lowestPriceBrandDto.getLowestPriceBrandCategoryDto().getBrand(), "최저가 브랜드 정보가 예상과 다릅니다.");
        assertEquals("36,100", lowestPriceBrandDto.getLowestPriceBrandCategoryDto().getTotalPrice(), "최저가 총액 정보가 예상과 다릅니다.");
        assertEquals(8, lowestPriceBrandDto.getLowestPriceBrandCategoryDto().getCategories().size(), "최저가 카테고리 갯수가 예상과 다릅니다.");
    }

    @Test
    @DisplayName("POST /v1/products/brands - 성공")
    void createCategorySuccess() throws Exception {

        deleteCategorySuccess();
        RequestBrandCategoryDto requestBrandCategoryDto = RequestBrandCategoryDto.builder()
            .brand("A")
            .category("상의")
            .price(1000L)
            .build();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/v1/products/brands")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestBrandCategoryDto))
            )
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();

        BrandCategoryEntity createBrandCategoryEntity = BrandCategoryEntity.builder()
            .id(BrandCategoryId.builder()
                .brand(requestBrandCategoryDto.getBrand())
                .category(requestBrandCategoryDto.getCategory())
                .build())
            .price(requestBrandCategoryDto.getPrice())
            .build();

        when(brandCategoryRepository.findByIdBrandAndIdCategory("A", "상의")).thenReturn(Optional.of(createBrandCategoryEntity));
    }

    @Test
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
        assertNotNull(brandCategoryEntity, "브랜드/카테고리 정보가 존재 하지 않습니다.");

        assertEquals("A", brandCategoryEntity.get().getId().getBrand(), "브랜드 정보가 예상과 다릅니다.");
        assertEquals("상의", brandCategoryEntity.get().getId().getCategory(), "카테고리 정보가 예상과 다릅니다.");
        assertEquals(1001L, brandCategoryEntity.get().getPrice(), "가격 정보가 예상과 다릅니다.");

    }

    @Test
    @DisplayName("DELETE /v1/products/brands - 성공")
    void deleteCategorySuccess() throws Exception {
        RequestBrandCategoryDto requestBrandCategoryDto = RequestBrandCategoryDto.builder()
            .brand("A")
            .category("상의")
            .price(1000L)
            .build();

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/v1/products/brands")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(requestBrandCategoryDto))
            )
            .andExpect(MockMvcResultMatchers.status().isNoContent())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();

    }
}