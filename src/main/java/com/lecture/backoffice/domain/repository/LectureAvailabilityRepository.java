package com.lecture.backoffice.domain.repository;

import com.lecture.common.domain.model.LectureAvailability;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class LectureAvailabilityRepository {

    @PersistenceContext
    private EntityManager em;

    public LectureAvailability save(LectureAvailability availability) {
        em.persist(availability);
        return availability;
    }

    /**
     * 조건부 UPDATE를 통해 availableSeats를 1 감소시킵니다.
     * 만약 availableSeats가 0 이하이면 업데이트되지 않으므로, 반환된 행 수가 0이 됩니다.
     *
     * @param lectureId 강연 ID
     * @return 업데이트된 행 수 (1이면 성공, 0이면 좌석 부족)
     */
    public int decrementAvailableSeats(Long lectureId) {
        String sql = "UPDATE lecture_availability " +
                "SET available_seats = available_seats - 1, " +
                "    reserved_count = reserved_count + 1, " +
                "    updated_at = :now " +
                "WHERE lecture_id = :lectureId AND available_seats > 0";
        return em.createNativeQuery(sql)
                .setParameter("lectureId", lectureId)
                .setParameter("now", LocalDateTime.now())
                .executeUpdate();
    }
    /**
     * 예약 취소 시, 좌석 수를 복구하는 원자적 UPDATE 쿼리
     *
     * @param lectureId 강연 ID
     * @return 업데이트된 행 수 (1이면 성공)
     */
    public int incrementAvailableSeats(Long lectureId) {
        String sql = "UPDATE lecture_availability " +
                "SET available_seats = available_seats + 1, " +
                "    reserved_count = reserved_count - 1, " +
                "    updated_at = :now " +
                "WHERE lecture_id = :lectureId";
        return em.createNativeQuery(sql)
                .setParameter("lectureId", lectureId)
                .setParameter("now", LocalDateTime.now())
                .executeUpdate();
    }
}
