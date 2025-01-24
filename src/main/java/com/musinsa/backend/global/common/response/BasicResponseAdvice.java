package com.musinsa.backend.global.common.response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.musinsa.backend.global.common.exception.ServiceException;
import com.musinsa.backend.global.enums.ErrorCode;

@RestControllerAdvice
public class BasicResponseAdvice implements ResponseBodyAdvice<Object> {

    public final static String MSG_BAD_REQUEST = "유효한 요청이 아닙니다.";
    public final static String MSG_INTERNAL_SERVER_ERROR = "시스템 오류가 발생했습니다.";
    public final static String MSG_NOT_FOUND = "잘못된 요청입니다.";

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
        Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
        ServerHttpResponse response) {
        if (body instanceof BasicResponseDTO) {
            return body;
        }

        // Swagger Uri 예외 처리
        if (request.getURI().getPath().contains("v3/api-docs")) {
            return body;
        }
        if (request.getURI().getPath().contains("dashboard/download")) {
            return body;
        }

        return BasicResponseUtil.success(body);
    }

    /**
     * 커스텀 Exception
     */
    @ExceptionHandler(ServiceException.class)
    public Object handleExceptionCustom(ServiceException e) {
        if (e.getHttpStatus() != null) {
            return ResponseEntity.status(e.getHttpStatus().value()).body(BasicResponseUtil.error(
                new BasicResponseDTO.BasicResponseError(e.getCode(), e.getErrorCode(), e.getMessage())));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(BasicResponseUtil.error(
            new BasicResponseDTO.BasicResponseError(e.getCode(), e.getErrorCode(), e.getMessage())));
    }

    /**
     * 유효성 검사 Exception(@Valid 어노테이션용)
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleExceptionNotValid(MethodArgumentNotValidException e) {
        final List<FieldError> fieldErrorList = e.getBindingResult().getFieldErrors();
        List<Map> result = new ArrayList<>();
        for (FieldError error : fieldErrorList) {
            Map map = new HashMap();
            map.put(error.getField(), error.getDefaultMessage());
            result.add(map);
        }
        return BasicResponseUtil.error(
            new BasicResponseDTO.BasicResponseError(HttpStatus.BAD_REQUEST.value(), ErrorCode.VL00005.code,
                MSG_BAD_REQUEST, result.toString()));
    }

    /**
     * 400 Exception
     */
    @ExceptionHandler({
        MissingServletRequestParameterException.class,
        ServletRequestBindingException.class,
        TypeMismatchException.class,
        HttpMessageNotReadableException.class,
        MissingServletRequestPartException.class,
        BindException.class,
        HttpRequestMethodNotSupportedException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object handleException400(Exception e) {
        e.printStackTrace();
        return BasicResponseUtil.error(
            new BasicResponseDTO.BasicResponseError(HttpStatus.BAD_REQUEST.value(), ErrorCode.VL00005,
                MSG_BAD_REQUEST));
    }

    /**
     * 404 Exception
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object handleException404(NoHandlerFoundException e) {
        e.printStackTrace();
        return BasicResponseUtil.error(
            new BasicResponseDTO.BasicResponseError(HttpStatus.NOT_FOUND.value(), ErrorCode.VL00005, MSG_NOT_FOUND));
    }

    /**
     * 500 Exception
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object handleException500(Exception e) {
        e.printStackTrace();
        return BasicResponseUtil.error(
            new BasicResponseDTO.BasicResponseError(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.SE00004,
                MSG_INTERNAL_SERVER_ERROR));
    }
}
