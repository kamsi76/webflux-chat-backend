package com.company.chat.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.company.chat.repository.ChatRoomUserRepository;
import com.company.chat.repository.UserRepository;
import com.company.common.database.entity.ChatRoomUser;
import com.company.common.database.entity.User;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * 채팅 참여자 관리 서비스
 */
@Service
@RequiredArgsConstructor
public class ChatRoomParticipantService {

	private final ChatRoomUserRepository chatRoomUserRepository;

	private final UserRepository userRepository;

	/**
	 * ChatroomUser를 조회 한 후에 user 를 조회하여 목록을 반환한다.
	 * @param roomId
	 * @return
	 */
	public Mono<List<User>> findParticipantsByRoomId(Long roomId) {
	    return chatRoomUserRepository.findByChatroomId(roomId)         // Flux<ChatRoomUser>
	             .flatMap(rel -> userRepository.findById(rel.getUserId())) // Flux<User>
	             .collectList(); // Mono<List<User>>
	}

	/**
	 * 채팅방 참여자 정보를 등록한다.
	 * 등록되어 있는지 여부를 판단하여 등록되어 있지 않은 경우에만 등록을 하도록 한다.
	 * @param roomId
	 * @param userId
	 * @return
	 */
	public Mono<Void> registerParticipant(Long roomId, Long userId) {

	    return chatRoomUserRepository.existsByChatroomIdAndUserId(roomId, userId)
	            .flatMap(exists -> {

	                if (exists) {
	                    return Mono.empty(); // 이미 등록되어 있음
	                }

	                ChatRoomUser relation = ChatRoomUser.builder()
	                                        .chatroomId(roomId)
	                                        .userId(userId)
	                                        .build();
	                return chatRoomUserRepository.save(relation).then();
	            });

	}

	/**
	 * 참여자를 삭제 처리한다.
	 * @param roomId
	 * @param userId
	 * @return
	 */
	public Mono<Void> removeParticipant(Long roomId, Long userId) {
		return chatRoomUserRepository.deleteByChatroomIdAndUserId(roomId, userId);
	}
}
