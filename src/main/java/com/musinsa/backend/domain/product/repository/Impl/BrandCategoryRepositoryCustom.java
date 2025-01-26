package com.musinsa.backend.domain.product.repository.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.musinsa.backend.domain.product.dto.CategoryPriceLowestAndHighestDto;
import com.musinsa.backend.domain.product.entity.BrandCategoryEntity;

/**
 * Created by kimkh
 */
@Repository
public interface BrandCategoryRepositoryCustom {
	List<BrandCategoryEntity> findLowestPriceBrand();
	CategoryPriceLowestAndHighestDto findCategoryPriceLowestAndHighest(String categoryName);
}
