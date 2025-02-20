package com.lecture.front.domain.repository;

import com.lecture.common.domain.model.Reservation;
import com.lecture.common.domain.model.ReservationStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository("frontReservationRepository")
public class ReservationRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * 예약 엔티티 저장 (신규 등록 또는 업데이트)
     * @param reservation 저장할 예약 엔티티
     * @return 저장된 Reservation 엔티티
     */
    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            em.persist(reservation);
            return reservation;
        } else {
            return em.merge(reservation);
        }
    }

    /**
     * 사번으로 예약 목록 조회
     * @param employeeNumber 예약된 사번
     * @return 해당 사번에 해당하는 Reservation 리스트
     */
    public List<Reservation> findByEmployeeNumber(String employeeNumber) {
        return em.createQuery("select r from Reservation r where r.employeeNumber = :employeeNumber", Reservation.class)
                .setParameter("employeeNumber", employeeNumber)
                .getResultList();
    }

    /**
     * 특정 강연의 예약 수 조회 (모든 예약 건수)
     * @param lectureId 강연 ID
     * @return 예약 건수
     */
    public long countByLectureId(Long lectureId) {
        return em.createQuery("select count(r) from Reservation r where r.lectureId = :lectureId", Long.class)
                .setParameter("lectureId", lectureId)
                .getSingleResult();
    }

    /**
     * 강연 ID와 사번을 기준으로 예약을 조회
     * @param lectureId 강연 ID
     * @param employeeNumber 사번
     * @return 조건에 맞는 Reservation 엔티티, 없으면 null 반환
     */
    public Reservation findByLectureIdAndEmployeeNumber(Long lectureId, String employeeNumber) {
        List<Reservation> results = em.createQuery("select r from Reservation r where r.lectureId = :lectureId and r.employeeNumber = :employeeNumber", Reservation.class)
                .setParameter("lectureId", lectureId)
                .setParameter("employeeNumber", employeeNumber)
                .getResultList();
        return results.isEmpty() ? null : results.getFirst();
    }

    /**
     * 특정 기간 내에 해당 강연에 대한 예약 건수를 조회
     * 주로 인기 강연 조회 시 사용됩니다.
     * @param lectureId 강연 ID
     * @param from 시작 시간
     * @param to 종료 시간
     * @return 해당 기간 내 예약 건수
     */
    public long countReservationsForLectureWithinPeriod(Long lectureId, LocalDateTime from, LocalDateTime to) {
        return em.createQuery("select count(r) from Reservation r where r.lectureId = :lectureId and r.createdAt between :from and :to", Long.class)
                .setParameter("lectureId", lectureId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();
    }

    /**
     * 강연 ID와 사번을 기준으로 활성 예약(예약 상태가 CONFIRMED인 경우)이 존재하는지 확인
     * @param lectureId 강연 ID
     * @param employeeNumber 사번
     * @return 활성 예약이 존재하면 true, 없으면 false
     */
    public boolean existsActiveReservationByLectureIdAndEmployeeNumber(Long lectureId, String employeeNumber) {
        Long count = em.createQuery("select count(r) from Reservation r where r.lectureId = :lectureId and r.employeeNumber = :employeeNumber and r.status = :status", Long.class)
                .setParameter("lectureId", lectureId)
                .setParameter("employeeNumber", employeeNumber)
                .setParameter("status", ReservationStatus.CONFIRMED)
                .getSingleResult();
        return count > 0;
    }
}
