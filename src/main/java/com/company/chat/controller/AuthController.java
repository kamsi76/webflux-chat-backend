package com.company.chat.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.chat.dto.AuthRequest;
import com.company.chat.service.AuthService;
import com.company.common.database.entity.User;
import com.company.common.dto.ApiResponse;
import com.company.common.utils.JwtUtil;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;
	private final JwtUtil jwtUtil;
	
	/**
	 * 회원가입 처리
	 * @param user
	 * @return
	 */
	@PostMapping("/signup")
	public Mono<ResponseEntity<ApiResponse<User>>> signup(@RequestBody User user) {
		
		return authService.signup(user)
					.map(savedUser -> ResponseEntity.ok().body(new ApiResponse<>(true, "회원가입성공", savedUser)))
					.onErrorResume(error -> Mono.just(ResponseEntity.badRequest().body(new ApiResponse<>(false, error.getMessage(), null))));
		
	}
	
	/**
	 * 로그인 처리
	 * 
	 * 사용자 정보가 있는 경우
	 * jwt token을 생성하고 token 정보와 user 정보를 같이 프론트엔드로 전달한다.
	 * @param request
	 * @return
	 * {
	 * 	"token" : "토큰값",
	 * 	"user" : 
	 * 		{
	 * 			"id" : 12345,
	 * 			"username" : "testid",
	 * 			"password" : null,
	 * 			"nickname" : "nickname",
	 * 			"createAt" : 2025-05-02 00:00:00
	 * 		}
	 * }
	 */
	@PostMapping("/login")
	public Mono<ResponseEntity<ApiResponse<Map<String, Object>>>> login(@RequestBody AuthRequest request) {

		return authService.login(request)
					.map(user -> {
						String token = jwtUtil.generateToken(user.getUsername());
						Map<String, Object> data = new HashMap<String, Object>();
						data.put("token", token);
						
						user.setPassword(null);	// 비밀번호는 빼고 전달
						data.put("user", user);
						
						return ResponseEntity.ok(new ApiResponse<>(true, "로그인 성공!!", data));
					})
					.onErrorResume(error -> Mono.just(
													ResponseEntity
															.badRequest()
															.body(new ApiResponse<>(false, error.getMessage(), null))
												));
	}
}
