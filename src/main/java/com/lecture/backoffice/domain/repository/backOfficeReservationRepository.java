package com.lecture.backoffice.domain.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Reservation 엔티티의 CRUD 및 커스텀 쿼리 작업을 위한 Repository
 */
@Repository
public class backOfficeReservationRepository {

    @PersistenceContext
    private EntityManager em;

    // 강연 ID에 해당하는 예약의 사번 목록 조회
    public List<String> findEmployeeNumbersByLectureId(Long lectureId) {
        return em.createQuery("select r.employeeNumber from Reservation r where r.lecture.id = :lectureId", String.class)
                .setParameter("lectureId", lectureId)
                .getResultList();
    }
}