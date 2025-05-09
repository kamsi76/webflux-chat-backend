package com.company.chat.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.company.chat.repository.ChatRoomRepository;
import com.company.chat.repository.ChatRoomUserRepository;
import com.company.common.database.entity.ChatRoom;
import com.company.common.database.entity.ChatRoomUser;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 채팅방 생성 및 조회 서비스
 */
@Service
@RequiredArgsConstructor
public class ChatRoomService {

	private final ChatRoomRepository chatRoomRepository;
	private final ChatRoomUserRepository chatRoomUserRepository;

    // 채팅방 생성
    public Mono<ChatRoom> createRoom(String name, Long userId) {

        ChatRoom room = ChatRoom.builder()
                .name(name)
                .creatorId(userId)
                .createdAt(LocalDateTime.now())
                .build();
        return chatRoomRepository.save(room);
    }

    /**
     * 모든 채팅방 목록 조회
     * @return
     */
    public Flux<ChatRoom> selectAllRooms() {
        return chatRoomRepository.findAll();
    }

    /**
     * 나의 채팅방 목록 조회
     * @return
     */
    public Flux<ChatRoom> selectMyChateRooms(Long userId) {
        return chatRoomUserRepository.findByUserId(userId)      // Flux<ChatRoomUser>
                .map(ChatRoomUser::getChatroomId)               // Flux<Long>
                .collectList()                                  // Mono<List<Long>>
                .flatMapMany(chatRoomRepository::findByIdIn);   // Flux<ChatRoom>
    }

}
