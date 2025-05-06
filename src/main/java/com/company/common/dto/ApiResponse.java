package com.company.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 프론트엔드로 데이터를 전송하기 위한 DTO Class
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
	
	private boolean success; // 성공여부
	
	private String message;	// 전송 메시지
	
	private T data;			// 전송 데이터
	
}
