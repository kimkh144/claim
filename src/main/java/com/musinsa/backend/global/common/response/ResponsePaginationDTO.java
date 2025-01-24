package com.musinsa.backend.global.common.response;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by kimkh on 2024. 5. 21..
 */
@Schema(title = "페이지 정보")
@AllArgsConstructor
@Getter
@Builder
public class ResponsePaginationDTO {
	@Schema(description = "전체 아이템수")
	private int totalCount;

	@Schema(description = "아이템수")
	private int currentCount;

	@Schema(description = "현재 페이지")
	private int currentPage;

	@Schema(description = "마지막 페이지")
	public int getTotalPage() {
		if (totalCount == 0) {
			return 1;
		} else {
			int totalPages = totalCount / limit;

			if (totalCount % limit > 0) {
				totalPages++;
			}

			return totalPages;
		}
	}

	@JsonIgnore
	private int limit;
}
