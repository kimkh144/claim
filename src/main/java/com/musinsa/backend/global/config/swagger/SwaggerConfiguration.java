package com.musinsa.backend.global.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;

@Configuration
public class SwaggerConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("[MUSINSA] Java(Kotlin) Backend Engineer - 과제")
                .version("v1")
                .contact(new Contact()
                    .name("김규현")
                    .email("bite111@naver.com")
                )
                .description("""        
                        [과제]
                        무신사는 다음 8개의 카테고리에서 상품을 하나씩 구매하여, 코디를 완성하는 서비스를 준비중입니다.
                        | 카테고리   | 상의     | 아우터   | 바지   | 스니커즈    | 가방    | 모자    | 양말    | 액세서리   |
                        |----------|---------|--------|-------|-----------|--------|-------|---------|---------|
                        | 브랜드 A   | 11,200 | 5,500  | 4,200  | 9,000    | 2,000  | 1,700  | 1,800  | 2,300    |
                        | 브랜드 B   | 10,500 | 5,900  | 3,800  | 9,100    | 2,100  | 2,000  | 2,000  | 2,200    |
                        | 브랜드 C   | 10,000 | 6,200  | 3,300  | 9,200    | 2,200  | 1,900  | 2,200  | 2,100    |
                        | 브랜드 D   | 10,100 | 5,100  | 3,000  | 9,500    | 2,500  | 1,500  | 2,400  | 2,000    |
                        | 브랜드 E   | 10,700 | 5,000  | 3,800  | 9,900    | 2,300  | 1,800  | 2,100  | 2,100    |
                        | 브랜드 F   | 11,200 | 7,200  | 4,000  | 9,300    | 2,100  | 1,600  | 2,300  | 1,900    |
                        | 브랜드 G   | 10,500 | 5,800  | 3,900  | 9,000    | 2,200  | 1,700  | 2,100  | 2,000    |
                        | 브랜드 H   | 10,800 | 6,300  | 3,100  | 9,700    | 2,100  | 1,600  | 2,000  | 2,000    |
                        | 브랜드 I   | 11,400 | 6,700  | 3,200  | 9,500    | 2,400  | 1,700  | 1,700  | 2,400    |

                        이 데이터를 기반으로
                        1. 고객은 카테고리 별로 최저가격인 브랜드와 가격을 조회하고 총액이 얼마인지 확인할 수 있어야 합니다.
                        2. 고객은 단일 브랜드로 전체 카테고리 상품을 구매할 경우 최저가격인 브랜드와 총액이 얼마인지 확인할 수 있어야 합니다.
                        3. 고객은 특정 카테고리에서 최저가격 브랜드와 최고가격 브랜드를 확인하고 각 브랜드 상품의 가격을 확인할 수 있어야 합니다.
                        4. 운영자는 새로운 브랜드를 등록하고, 모든 브랜드의 상품을 추가, 변경, 삭제할 수 있어야 합니다.
                        이 4가지 기능을 사용할 수 있게 하는 4개의 API를 구현해야 합니다.
                """));
    }
}