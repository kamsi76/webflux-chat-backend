package com.company.chat.service;

import org.springframework.stereotype.Service;

import com.company.chat.repository.UserRepository;
import com.company.common.database.entity.User;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	public Mono<User> findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}
