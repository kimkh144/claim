package com.musinsa.backend.domain.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.musinsa.backend.domain.product.repository.Impl.BrandCategoryRepositoryCustom;
import com.musinsa.backend.domain.product.entity.BrandCategoryEntity;
import com.musinsa.backend.domain.product.entity.BrandCategoryId;

import jakarta.validation.constraints.NotBlank;

/**
 * Created by kimkh
 */
@Repository
public interface BrandCategoryRepository extends JpaRepository<BrandCategoryEntity, BrandCategoryId>,
	BrandCategoryRepositoryCustom {

	@Query(value = """
		SELECT TB_PRODUCT_BRAND_CATEGORY_RANKED.CATEGORY, TB_PRODUCT_BRAND_CATEGORY_RANKED.BRAND, TB_PRODUCT_BRAND_CATEGORY_RANKED.PRICE
		FROM (
		    SELECT
		        PBC.CATEGORY,
		        PBC.BRAND,
		        PBC.PRICE,
		        ROW_NUMBER() OVER (PARTITION BY PBC.CATEGORY ORDER BY PBC.PRICE ASC, PBC.BRAND DESC) AS ROW_NUM
		    FROM
		        TB_PRODUCT_BRAND_CATEGORY PBC
		) AS TB_PRODUCT_BRAND_CATEGORY_RANKED
		WHERE TB_PRODUCT_BRAND_CATEGORY_RANKED.ROW_NUM = 1
		ORDER BY TB_PRODUCT_BRAND_CATEGORY_RANKED.CATEGORY 
		""", nativeQuery = true)
	List<BrandCategoryEntity> findLowestPriceProductsByCategory();

	Optional<BrandCategoryEntity> findByIdBrandAndIdCategory(
		@NotBlank(message = "브랜드 정보는 필수 입력 되어야 합니다.") String brand,
		@NotBlank(message = "카테고리 정보는 필수 입력 되어야 합니다.") String category);
}
