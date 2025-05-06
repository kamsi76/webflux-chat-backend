package com.company.common.database.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * users 테이블과 매핑되는 사용자 엔티티 클래스입니다.
 */
@Table("users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	@Id private Long id;		// 사용자 ID (기본키)
	private String username;	// 사용자 로그인 ID
	private String password;	// 암호화된 비밀번호
	private String nickname;	// 사용자 별명 
	private LocalDateTime createAt;	//가입일시
}
