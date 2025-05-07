package com.company.chat.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.chat.service.ChatMessageService;
import com.company.common.database.document.ChatMessage;
import com.company.common.dto.ApiResponse;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * 채팅 메시지 조회 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chatroom")
public class ChatMessageController {

	private final ChatMessageService chatMessageService;

    /**
     * 특정 채팅방의 메시지 조회
     */
    @GetMapping("/{roomId}/messages")
    public Mono<ResponseEntity<ApiResponse<List<ChatMessage>>>> getMessagesByRoomId(@PathVariable Long roomId) {
    	return chatMessageService.findByRoomId(roomId)
				.collectList()
				.map(messages -> ResponseEntity.ok(new ApiResponse<>(true, "메시지 조회 성공", messages)) );
    }

}
