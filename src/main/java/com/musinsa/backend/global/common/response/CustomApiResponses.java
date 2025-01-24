package com.musinsa.backend.global.common.response;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.MediaType;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "400",
                description = BasicResponseAdvice.MSG_BAD_REQUEST,
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponseDTO.class))),
        @ApiResponse(
                responseCode = "404",
                description = BasicResponseAdvice.MSG_NOT_FOUND,
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponseDTO.class))),
        @ApiResponse(
                responseCode = "500",
                description = BasicResponseAdvice.MSG_INTERNAL_SERVER_ERROR,
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = BasicResponseDTO.class)))
})
public @interface CustomApiResponses {
}
