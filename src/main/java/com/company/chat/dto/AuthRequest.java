package com.company.chat.dto;

import lombok.Data;

/**
 * 로그인 요청 시 전달되는 JSON 데이터를 담는 DTO
 */
@Data
public class AuthRequest {

	private String username;	// 사용자 ID
	private String password;	// 비밀번호
}
