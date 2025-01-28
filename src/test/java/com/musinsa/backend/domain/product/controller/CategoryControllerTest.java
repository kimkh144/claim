package com.musinsa.backend.domain.product.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.charset.StandardCharsets;
import java.util.Map;

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

        CategoryPriceLowestAndHighestDto categoryPriceLowestAndHighestDto = new ObjectMapper()
            .treeToValue(
                new ObjectMapper().readTree(mvcResult.getResponse().getContentAsString()).get("data"),
                CategoryPriceLowestAndHighestDto.class
            );

        assertAll(
            "카테고리 가격 정보 검증",
            () -> assertNotNull(categoryPriceLowestAndHighestDto.getCategory(), "카테고리 정보가 존재하지 않습니다."),
            () -> assertNotNull(categoryPriceLowestAndHighestDto.getHighPrice(), "최고가 정보가 존재하지 않습니다."),
            () -> assertNotNull(categoryPriceLowestAndHighestDto.getLowerPrice(), "최저가 정보가 존재하지 않습니다."),
            () -> assertEquals("상의", categoryPriceLowestAndHighestDto.getCategory(), "카테고리 정보가 예상과 다릅니다."),
            () -> assertAll(
                "최저가 상세 정보 검증",
                () -> assertEquals("C", categoryPriceLowestAndHighestDto.getLowerPrice().get(0).getBrand(),
                    "최저가 브랜드 정보가 예상과 다릅니다."),
                () -> assertEquals("10,000", categoryPriceLowestAndHighestDto.getLowerPrice().get(0).getPrice(),
                    "최저가 가격 정보가 예상과 다릅니다.")
            ),
            () -> assertAll(
                "최고가 상세 정보 검증",
                () -> assertEquals("I", categoryPriceLowestAndHighestDto.getHighPrice().get(0).getBrand(),
                    "최고가 브랜드 정보가 예상과 다릅니다."),
                () -> assertEquals("11,400", categoryPriceLowestAndHighestDto.getHighPrice().get(0).getPrice(),
                    "최고가 가격 정보가 예상과 다릅니다.")
            )
        );
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

        BrandLowestPriceResultDto brandLowestPriceResultDto = new ObjectMapper()
            .readValue(
                new ObjectMapper().readTree(mvcResult.getResponse().getContentAsString()).get("data").toString(),
                BrandLowestPriceResultDto.class
            );

        assertAll(
            "상품 정보 및 총액 검증",
            () -> assertNotNull(brandLowestPriceResultDto.getProducts(), "상품 정보가 존재하지 않습니다."),
            () -> assertNotNull(brandLowestPriceResultDto.getTotalPrice(), "상품 총액이 존재하지 않습니다."),
            () -> assertEquals(8, brandLowestPriceResultDto.getProducts().size(), "카테고리 갯수가 예상과 다릅니다."),
            () -> assertEquals("34,100", brandLowestPriceResultDto.getTotalPrice(), "상품 총액이 예상과 다릅니다.")
        );

        // 카테고리별 기대값 정의
        Map<String, String[]> expectedValues = Map.of(
            "상의", new String[] {"C", "10,000"},
            "아우터", new String[] {"E", "5,000"},
            "바지", new String[] {"D", "3,000"},
            "스니커즈", new String[] {"G", "9,000"},
            "가방", new String[] {"A", "2,000"},
            "모자", new String[] {"D", "1,500"},
            "양말", new String[] {"I", "1,700"},
            "액세서리", new String[] {"F", "1,900"}
        );

        // 상품별 검증
        brandLowestPriceResultDto.getProducts().forEach(productPriceDto -> {
            String category = productPriceDto.getCategory();
            assertAll(
                String.format("카테고리 '%s' 검증", category),
                () -> assertNotNull(category, "상품의 카테고리 정보가 존재하지 않습니다."),
                () -> assertNotNull(productPriceDto.getBrand(), "상품의 브랜드 정보가 존재하지 않습니다."),
                () -> assertNotNull(productPriceDto.getPrice(), "상품의 가격 정보가 존재하지 않습니다."),
                () -> {
                    String[] expected = expectedValues.get(category);
                    assertNotNull(expected, String.format("예상값이 정의되지 않은 카테고리입니다: %s", category));
                    assertEquals(expected[0], productPriceDto.getBrand(),
                        String.format("최저가 %s 브랜드 정보가 예상과 다릅니다.", category));
                    assertEquals(expected[1], productPriceDto.getPrice(),
                        String.format("최저가 %s 가격 정보가 예상과 다릅니다.", category));
                }
            );
        });

    }
}