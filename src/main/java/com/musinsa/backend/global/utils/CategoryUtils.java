package com.musinsa.backend.global.utils;

import java.util.List;

/**
 * Created by kimkh on 2024. 10. 31..
 */
public class CategoryUtils {
	private CategoryUtils() {
		throw new IllegalStateException("Utility class");
	}
	public static final List<String> VALID_CATEGORIES = List.of("상의", "아우터", "바지", "스니커즈", "가방", "모자", "양말", "액세서리");

	public static boolean isValidCategory(String categoryName) {
		return VALID_CATEGORIES.contains(categoryName);
	}
}
