package com.lecture.front.domain.model;

import com.lecture.common.domain.model.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 강연 예약 엔티티: 특정 강연에 대해 신청한 사번 및 예약 상태 저장
 */
@Entity(name = "frontReservation")
@Table(name = "reservations", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"lecture_id", "employeeNumber"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder  // 빌더 패턴 적용
public class Reservation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 예약이 속한 강연의 ID
    @Column(name = "lecture_id")
    private Long lectureId;

    // 신청한 사번 (5자리)
    @Size(min=5, max=5)
    private String employeeNumber;

    // 예약 상태
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;
}
