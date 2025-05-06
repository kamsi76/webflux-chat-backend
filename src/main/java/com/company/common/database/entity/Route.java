package com.company.common.database.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;

/**
 * routes 테이블과 매핑되는 라우터 엔티티
 */
@Table("routes")
@Data
public class Route {
	
    @Id
    private Long id; // 내부적으로 PK 역할 (Auto Increment)

    private String path;

    private String componentName;
}
