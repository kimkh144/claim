package com.musinsa.backend.domain.product.repository.Impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.musinsa.backend.domain.product.dto.ProductBrandPriceDto;
import com.musinsa.backend.domain.product.dto.CategoryPriceLowestAndHighestDto;
import com.musinsa.backend.domain.product.entity.BrandCategoryEntity;
import com.musinsa.backend.domain.product.entity.QBrandCategoryEntity;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

/**
 * Created by kimkh
 */
@Repository
@RequiredArgsConstructor
public class BrandCategoryRepositoryCustomImpl implements BrandCategoryRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
	private static final QBrandCategoryEntity qBrandCategoryEntity = QBrandCategoryEntity.brandCategoryEntity;

    @Override
    public List<BrandCategoryEntity> findLowestPriceBrand() {
        /* 단일 브랜드 상품의 카테고리 총합 최소가 브랜드 조회 */
        String lowestPriceBrand = jpaQueryFactory
            .select(qBrandCategoryEntity.id.brand)
            .from(qBrandCategoryEntity)
            .groupBy(qBrandCategoryEntity.id.brand)
            .orderBy(qBrandCategoryEntity.price.sum().asc())
            .fetchFirst();

        /* 단일 브랜드 카테고리, 상품가 조회 */
        return
            jpaQueryFactory.select(
                    qBrandCategoryEntity
                )
                .from(qBrandCategoryEntity)
                .where(qBrandCategoryEntity.id.brand.eq(lowestPriceBrand))
                .fetch();
    }

    /*

     */
    @Override
    public CategoryPriceLowestAndHighestDto findCategoryPriceLowestAndHighest(String category){
        List<ProductBrandPriceDto> minPrice = findBrandPriceByCategory(category, false);
        List<ProductBrandPriceDto> maxPrice = findBrandPriceByCategory(category, true);

        return CategoryPriceLowestAndHighestDto.builder()
            .category(category)
            .lowerPrice(minPrice)
            .highPrice(maxPrice)
            .build();
    }

    /* 상품 가격 정보 조회 */
    private List<ProductBrandPriceDto> findBrandPriceByCategory(String category, boolean isMax) {
        /* 카테고리의 상품 최저, 최대 가격 조회 */
        Long price = jpaQueryFactory
            .select((isMax) ? qBrandCategoryEntity.price.max() : qBrandCategoryEntity.price.min())
            .from(qBrandCategoryEntity)
            .where(qBrandCategoryEntity.id.category.eq(category))
            .fetchOne();

        /* 카테고리 최저, 최대 가격의 브랜드 정보 조회 */
        return
            jpaQueryFactory.select(Projections.constructor(ProductBrandPriceDto.class,
                    qBrandCategoryEntity.id.brand.as("brand"),
                    qBrandCategoryEntity.price.as("price")
                ))
                .from(qBrandCategoryEntity)
                .where(qBrandCategoryEntity.id.category.eq(category).and(qBrandCategoryEntity.price.eq(price)))
                .orderBy(qBrandCategoryEntity.id.category.asc())
                .fetch();
    }
}
