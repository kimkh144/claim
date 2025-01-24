package com.musinsa.backend.global.constants.swagger;

/**
 * Created by kimkh on 2024. 11. 1..
 */
public class SwaggerConstants {

	public static final String PREVENT_MESSAGE = "Utility class cannot be instantiated";

	/* Prevent instantiation */
	private SwaggerConstants() {
		throw new UnsupportedOperationException(PREVENT_MESSAGE);
	}

    public static final String HTTP_STATUS_OK = "200";

    /* BRAND API */
    public static class Brand {
		private Brand() {
			throw new UnsupportedOperationException(PREVENT_MESSAGE);
		}
        public static final String BRAND_TAG = "상품 - 브랜드";
        public static final String BRAND_V1 = "/v1/products/brands";

        /* 구현 2) - 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API */
        public static final String BRAND_API_V1_URL = "lowest-price";
        public static final String BRAND_API_V1_VERSION = "구현 2)";
        public static final String BRAND_API_V1_DESC = "- 단일 브랜드로 모든 카테고리 상품을 구매할 때 최저가격에 판매하는 브랜드와 카테고리의 상품가격, 총액을 조회하는 API";

        /* 구현 4) - 브랜드 및 상품을 추가 / 업데이트 / 삭제하는 API */
        public static final String BRAND_API_V4_URL = "";
        public static final String BRAND_API_V4_VERSION = "구현 4)";
        public static final String BRAND_API_V4_CREATE_DESC = "- 브랜드 및 상품 추가 API";
        public static final String BRAND_API_V4_UPDATE_DESC = "- 브랜드 및 상품 수정 API";
        public static final String BRAND_API_V4_DELETE_DESC = "- 브랜드 및 상품 삭제 API";
    }

    /* CATEGORY API */
    public static class Category {
		private Category() {
			throw new UnsupportedOperationException(PREVENT_MESSAGE);
		}
        public static final String CATEGORY_TAG = "상품 - 카테고리";
        public static final String CATEGORY_V1 = "v1/products/categories";

        /* 구현 1) - 테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API */
        public static final String CATEGORY_API_V1_URL = "/lowest-price/brands";
        public static final String CATEGORY_API_V1_VERSION = "구현 1)";
        public static final String CATEGORY_API_V1_DESC = "- 카테고리 별 최저가격 브랜드와 상품 가격, 총액을 조회하는 API";

        /*구현 3) - 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API*/
        public static final String CATEGORY_API_V3_URL = "{category}/price-range/brands";
        public static final String CATEGORY_API_V3_VERSION = "구현 3)";
        public static final String CATEGORY_API_V3_DESC = "- 카테고리 이름으로 최저, 최고 가격 브랜드와 상품 가격을 조회하는 API";
    }
}
