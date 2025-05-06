package com.company.chat.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.company.common.database.entity.User;

import reactor.core.publisher.Mono;

/**
 * 사용자 정보를 PostgreSQL에서 CRUD 하기 위한 Reactive Repository
 */
public interface UserRepository extends ReactiveCrudRepository<User, Long>{
	
	// username으로 사용자 조회
	Mono<User> findByUsername(String username);

}
