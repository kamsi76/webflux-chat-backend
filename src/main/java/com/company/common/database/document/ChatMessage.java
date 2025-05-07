package com.company.common.database.document;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * MongoDB에 저장될 채팅 메시지 도큐먼트
 */
@Document(collection = "chat_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {
    @Id
    private String id;

    private Long roomId;             // 채팅방 ID

    private String sender;           // 보낸 사람 닉네임

    private String content;          // 메시지 내용

    private String avatar;           // 아바타 이미지 URL

    private LocalDateTime timestamp; // 전송 시각
}