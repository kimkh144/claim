package com.musinsa.backend.global.common.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.musinsa.backend.global.enums.ErrorCode;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(title = "공통 응답")
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class BasicResponseDTO<T> implements Serializable {

    @Schema(title = "pagination", description = "메타정보")
    public T meta;

    @Schema(description = "데이터")
    public T data;

    @Schema(description = "에러 정보")
    public BasicResponseError error;

    @Builder
    public BasicResponseDTO(T meta, T data) {
        this.meta = meta;
        this.data = data;
    }

    @Schema(title = "공통응답 - 에러 정보")
    @AllArgsConstructor
    @Getter
    public static class BasicResponseError implements Serializable{

        @Schema(description = "status 코드")
        private Integer code;

        @Schema(description = "코드")
        private String errorCode;

        @Schema(description = "메세지")
        private String message;

        @Schema(description = "@Valid 정보")
        private String validation;

        public BasicResponseError(Integer code, ErrorCode errorCode, String message) {
            this.code = code;
            this.errorCode = errorCode.code;
            this.message = message;
        }
    }
}
