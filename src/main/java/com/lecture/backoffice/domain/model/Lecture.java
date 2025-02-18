package com.lecture.backoffice.domain.model;

import com.lecture.common.domain.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 강연 엔티티: 강연자, 강연장, 신청 인원, 강연 시간, 강연 내용
 */
@Entity(name = "backofficeLecture")
@Table(name = "lectures")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder  // 빌더 패턴을 활용해 객체 생성 용이
public class Lecture extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 강연자 이름
    private String lecturer;
    // 강연장(장소)
    private String venue;
    // 신청 인원(강연에 입장 가능한 인원 수)
    private int capacity;

    // 강연 시작 시간
    private LocalDateTime startTime;
    // 강연 종료 시간
    private LocalDateTime endTime;

    // 강연 내용 (최대 1000자)
    @Column(length = 1000)
    private String description;
}
