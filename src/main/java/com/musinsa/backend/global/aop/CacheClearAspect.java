package com.musinsa.backend.global.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Created by kimkh
 */
@Aspect
@Component
@RequiredArgsConstructor
public class CacheClearAspect {
    private final CacheManager cacheManager;

    @After("@annotation(ClearCache)")
    public void clearCache(org.aspectj.lang.JoinPoint joinPoint) {
        // 메서드 어노테이션 읽기
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        ClearCache clearCache = method.getAnnotation(ClearCache.class);

        // 어노테이션에 설정된 캐시 이름 가져오기
        String[] cacheNames = clearCache.value();
        for (String cacheName : cacheNames) {
            if (cacheManager.getCache(cacheName) != null) {
                cacheManager.getCache(cacheName).clear();
                System.out.println("Cache '" + cacheName + "' cleared via AOP.");
            } else {
                System.out.println("Cache '" + cacheName + "' not found.");
            }
        }
    }
}
