package com.lecture.backoffice.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 모든 엔티티에서 공통으로 사용하는 생성일 관리용 BaseEntity
 */
@Getter
@MappedSuperclass
public abstract class BaseEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // 엔티티가 최초 저장되기 전 실행 (생성일 설정)
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
