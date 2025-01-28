package com.musinsa.backend.global.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.musinsa.backend.domain.product.service.BrandCategoryService;

import lombok.RequiredArgsConstructor;

/**
 * Created by kimkh on 2025. 1. 28..
 */
@Component
@RequiredArgsConstructor
public class AppStartupWarmupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(AppStartupWarmupRunner.class);

    private final BrandCategoryService brandCategoryService;

    /* 캐시 데이터 로드 작업 */
    private void warmupCache() {
        logger.info("캐시 데이터 준비 중.");
        brandCategoryService.getCategoryPriceLowestAndHighest("상의");
        brandCategoryService.getLowestPriceProductsByBrand();
        brandCategoryService.getLowestPriceProductsByCategory();
        logger.info("캐시 데이터 준비 완료.");
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("AppStartupWarmupRunner 실행 중...");
        warmupCache();
        logger.info("AppStartupWarmupRunner 작업 완료.");
    }
}
