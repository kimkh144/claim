package com.musinsa.backend.global.utils;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by kimkh on 10/30/24.
 */
public class StringFormatUtils {

	private StringFormatUtils() {
		throw new IllegalStateException("Utility class");
	}
	public static String setPriceComma(String price) {
		long amount = Long.parseLong(price); // 문자열을 long 타입으로 변환
		NumberFormat formatter = NumberFormat.getInstance(Locale.KOREA);
		return formatter.format(amount);
	}
}
