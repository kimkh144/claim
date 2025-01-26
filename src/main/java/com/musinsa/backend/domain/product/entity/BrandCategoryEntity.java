package com.musinsa.backend.domain.product.entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by kimkh on 2024. 10. 29..
 */
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Table(name = "TB_PRODUCT_BRAND_CATEGORY")
public class BrandCategoryEntity {

	@EmbeddedId
	private BrandCategoryId id;

    @Column(name = "PRICE", nullable = false)
    @Builder.Default
    private Long price = 0L;

}
