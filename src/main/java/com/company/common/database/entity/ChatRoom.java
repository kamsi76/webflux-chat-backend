package com.company.common.database.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 채팅방 엔티티 (PostgreSQL)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table("chat_rooms")
public class ChatRoom {

	@Id
    private Long id;                     // 채팅방 ID (자동 증가)
    private String name;                // 채팅방 이름
    private LocalDateTime createdAt;    // 생성일시
}
