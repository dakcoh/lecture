package com.lecture.front.domain.repository;

import com.lecture.front.domain.model.Reservation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Reservation 엔티티의 CRUD 및 커스텀 쿼리 작업을 수행하는 Repository
 */
@Repository("frontReservationRepository")
public class ReservationRepository {

    @PersistenceContext
    private EntityManager em;

    public Reservation save(Reservation reservation) {
        if (reservation.getId() == null) {
            em.persist(reservation);
            return reservation;
        } else {
            return em.merge(reservation);
        }
    }

    public boolean existsByLectureIdAndEmployeeNumber(Long lectureId, String employeeNumber) {
        Long count = em.createQuery("select count(r) from frontReservation r where r.lectureId = :lectureId and r.employeeNumber = :employeeNumber", Long.class)
                .setParameter("lectureId", lectureId)
                .setParameter("employeeNumber", employeeNumber)
                .getSingleResult();
        return count > 0;
    }

    public List<Reservation> findByEmployeeNumber(String employeeNumber) {
        return em.createQuery("select r from frontReservation r where r.employeeNumber = :employeeNumber", Reservation.class)
                .setParameter("employeeNumber", employeeNumber)
                .getResultList();
    }

    public long countByLectureId(Long lectureId) {
        return em.createQuery("select count(r) from frontReservation r where r.lectureId = :lectureId", Long.class)
                .setParameter("lectureId", lectureId)
                .getSingleResult();
    }

    public Reservation findById(Long id) {
        return em.find(Reservation.class, id);
    }

    public long countReservationsForLectureWithinPeriod(Long lectureId, LocalDateTime from, LocalDateTime to) {
        return em.createQuery("select count(r) from frontReservation r where r.lectureId = :lectureId and r.createdAt between :from and :to", Long.class)
                .setParameter("lectureId", lectureId)
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();
    }
}
