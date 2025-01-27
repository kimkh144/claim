package com.musinsa.backend.domain.product.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;

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
import com.musinsa.backend.domain.product.dto.BrandLowestPriceResultDto;
import com.musinsa.backend.domain.product.dto.CategoryPriceLowestAndHighestDto;
import com.musinsa.backend.domain.product.dto.ProductPriceDto;

/**
 * Created by kimkh on 2025. 1. 26..
 */
@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("GET /v1/products/categories/상의/price-range/brands - 성공")
    void category() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/v1/products/categories/상의/price-range/brands")
                    .characterEncoding(StandardCharsets.UTF_8)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();

        CategoryPriceLowestAndHighestDto categoryPriceLowestAndHighestDto = new ObjectMapper().treeToValue(
            new ObjectMapper().readTree(mvcResult.getResponse().getContentAsString()).get("data"),
            CategoryPriceLowestAndHighestDto.class);

        assertNotNull(categoryPriceLowestAndHighestDto.getCategory(), "카테고리 정보가 존재 하지 않습니다.");
        assertNotNull(categoryPriceLowestAndHighestDto.getHighPrice(), "최고가 정보가 존재 하지 않습니다.");
        assertNotNull(categoryPriceLowestAndHighestDto.getLowerPrice(), "최저가 정보가 존재 하지 않습니다.");
        assertEquals("상의", categoryPriceLowestAndHighestDto.getCategory(), "카테고리 정보가 예상과 다릅니다.");
        assertEquals("C", categoryPriceLowestAndHighestDto.getLowerPrice().get(0).getBrand(), "최저가 브랜드 정보가 예상과 다릅니다.");
        assertEquals("10,000", categoryPriceLowestAndHighestDto.getLowerPrice().get(0).getPrice(),
            "최저가 가격 정보가 예상과 다릅니다.");
        assertEquals("I", categoryPriceLowestAndHighestDto.getHighPrice().get(0).getBrand(), "최고가 브랜드 정보가 예상과 다릅니다.");
        assertEquals("11,400", categoryPriceLowestAndHighestDto.getHighPrice().get(0).getPrice(),
            "최고가 가격 정보가 예상과 다릅니다.");
    }

    @Test
    @DisplayName("GET /v1/products/categories/lowest-price/brands - 성공")
    void getCategory() throws Exception {

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/v1/products/categories/lowest-price/brands")
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn();

        BrandLowestPriceResultDto brandLowestPriceResultDto = new ObjectMapper().treeToValue(
            new ObjectMapper().readTree(mvcResult.getResponse().getContentAsString()).get("data"),
            BrandLowestPriceResultDto.class);

        assertNotNull(brandLowestPriceResultDto.getProducts(), "상품 정보가 존재 하지 않습니다.");
        assertNotNull(brandLowestPriceResultDto.getTotalPrice(), "상품 총액이 존재 하지 않습니다.");
        assertEquals(8, brandLowestPriceResultDto.getProducts().size(), "카테고리 갯수가 예상과 다릅니다.");
        assertEquals("34,100", brandLowestPriceResultDto.getTotalPrice(), "상품 총액이 예상과 다릅니다.");

        /* 카테고리 별 검증 */
        for (ProductPriceDto productPriceDto : brandLowestPriceResultDto.getProducts()) {
            assertNotNull(productPriceDto.getCategory(), "상품의 카테고리 정보가 존재 하지 않습니다.");
            assertNotNull(productPriceDto.getBrand(), "상품의 브랜드 정보가 존재 하지 않습니다.");
            assertNotNull(productPriceDto.getPrice(), "상품의 가격 정보가 존재 하지 않습니다.");

            String categoryName = productPriceDto.getCategory();
            if (categoryName.equals("상의")) {
                assertEquals("C", productPriceDto.getBrand(),
                    String.format("최저가 %s 브랜드 정보가 예상과 다릅니다.", categoryName));
                assertEquals("10,000", productPriceDto.getPrice(),
                    String.format("최저가 %s 가격 정보가 예상과 다릅니다.", categoryName));
            } else if (categoryName.equals("아우터")) {
                assertEquals("E", productPriceDto.getBrand(),
                    String.format("최저가 %s 브랜드 정보가 예상과 다릅니다.", categoryName));
                assertEquals("5,000", productPriceDto.getPrice(),
                    String.format("최저가 %s 가격 정보가 예상과 다릅니다.", categoryName));
            } else if (categoryName.equals("바지")) {
                assertEquals("D", productPriceDto.getBrand(),
                    String.format("최저가 %s 브랜드 정보가 예상과 다릅니다.", categoryName));
                assertEquals("3,000", productPriceDto.getPrice(),
                    String.format("최저가 %s 가격 정보가 예상과 다릅니다.", categoryName));
            } else if (categoryName.equals("스니커즈")) {
                assertEquals("A", productPriceDto.getBrand(),
                    String.format("최저가 %s 브랜드 정보가 예상과 다릅니다.", categoryName));
                assertEquals("9,000", productPriceDto.getPrice(),
                    String.format("최저가 %s 가격 정보가 예상과 다릅니다.", categoryName));
            } else if (categoryName.equals("가방")) {
                assertEquals("A", productPriceDto.getBrand(),
                    String.format("최저가 %s 브랜드 정보가 예상과 다릅니다.", categoryName));
                assertEquals("2,000", productPriceDto.getPrice(),
                    String.format("최저가 %s 가격 정보가 예상과 다릅니다.", categoryName));
            } else if (categoryName.equals("모자")) {
                assertEquals("D", productPriceDto.getBrand(),
                    String.format("최저가 %s 브랜드 정보가 예상과 다릅니다.", categoryName));
                assertEquals("1,500", productPriceDto.getPrice(),
                    String.format("최저가 %s 가격 정보가 예상과 다릅니다.", categoryName));
            } else if (categoryName.equals("양말")) {
                assertEquals("I", productPriceDto.getBrand(),
                    String.format("최저가 %s 브랜드 정보가 예상과 다릅니다.", categoryName));
                assertEquals("1,700", productPriceDto.getPrice(),
                    String.format("최저가 %s 가격 정보가 예상과 다릅니다.", categoryName));
            } else if (categoryName.equals("액세서리")) {
                assertEquals("F", productPriceDto.getBrand(),
                    String.format("최저가 %s 브랜드 정보가 예상과 다릅니다.", categoryName));
                assertEquals("1,900", productPriceDto.getPrice(),
                    String.format("최저가 %s 가격 정보가 예상과 다릅니다.", categoryName));
            }
        }

    }
}