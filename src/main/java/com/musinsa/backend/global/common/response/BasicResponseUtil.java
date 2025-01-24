package com.musinsa.backend.global.common.response;

public class BasicResponseUtil {
	private BasicResponseUtil() {
		throw new IllegalStateException("Utility class");
	}

    public static <T> BasicResponseDTO<T> success(T response) {
        return new BasicResponseDTO<>(response, null);
    }

    public static BasicResponseDTO<?> error(BasicResponseDTO.BasicResponseError e) {
        return new BasicResponseDTO<>(null,null, e);
    }

}
