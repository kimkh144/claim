package com.musinsa.backend.domain.product.entity;

import java.io.Serializable;

import com.querydsl.core.types.dsl.StringExpression;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by kimkh
 */
@Getter
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class BrandCategoryId implements Serializable {
    private String brand;
    private String category;

    @Builder
    public BrandCategoryId(String brand, String category) {
        this.brand = brand;
        this.category = category;
    }
}
