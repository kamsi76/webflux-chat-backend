package com.company.chat.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.company.chat.dto.AuthRequest;
import com.company.chat.repository.UserRepository;
import com.company.common.database.entity.User;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
	
	/**
	 * 사용자 회원가입 처리
	 * 
	 * 1. 등록하려고 하는 username이 DB에 존재하는지 여부를 파악한다.
	 *    만약 발견된게 있으면 오류 메시지를 발생시킨다.
	 * 2. 비어 있는 경우에는 비밀번호를 암호화 하고 사용자를 등록한다.
	 * @param user
	 * @return
	 */
	public Mono<User> signup(User user) {
		
		return userRepository.findByUsername(user.getUsername())
						.flatMap(_ -> Mono.<User>error( new RuntimeException("이미 존재하는 사용자입니다.")))
						.switchIfEmpty(
									Mono.defer(() -> {
										// 사용자 정보 저장
										user.setPassword(passwordEncoder.encode(user.getPassword()));	//비밀번호 암호화
										user.setCreateAt(LocalDateTime.now());
										return userRepository.save(user);
									})
								);
	}
	
	/**
	 * 로그인 처리 및 JWT 발급 
	 * 
	 * 사용자 정보를 조회를 하고 있으면 비밀번호를 비교 한다.
	 * 일치하는 사용자가 아닌경우  
	 * @param request
	 * @return
	 */
	public Mono<User> login(AuthRequest request) {
		
		return userRepository.findByUsername(request.getUsername())
						.filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))	//비밀번호가 일치하면 통과
						.switchIfEmpty(Mono.<User>error(new RuntimeException("아이디 또는 비밀번호가 올바르지 않습니다.")));		//일치하지 않으면 오류 발생
	}
	
}
