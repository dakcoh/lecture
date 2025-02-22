package com.lecture.common.domain.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lecture_availability")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureAvailability extends BaseEntity {

    /**
     * lecture_id를 기본 키로 사용하며, 강연 테이블과 1:1 관계를 가집니다.
     */
    @Id
    @Column(name = "lecture_id")
    private Long lectureId;

    // 강연 엔티티와 1:1 연관관계 설정
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId  // LectureAvailability의 기본키가 Lecture의 id와 동일함을 명시
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    // 예약 가능한 좌석 수
    @Column(name = "available_seats", nullable = false)
    private int availableSeats;

    // 예약된 좌석 수
    @Column(name = "reserved_count", nullable = false)
    private int reservedCount;
}
