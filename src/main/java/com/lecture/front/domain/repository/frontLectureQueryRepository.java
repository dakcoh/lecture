package com.lecture.front.domain.repository;

import com.lecture.common.domain.model.Lecture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Lecture 엔티티의 CRUD 작업을 수행하는 Repository.
 * 공통 도메인 Lecture 엔티티를 사용하므로, JPQL 쿼리에서 엔티티 이름은 "Lecture"를 사용합니다.
 */
@Repository
public class frontLectureQueryRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * 강연을 조회
     *
     * @param id 강연 ID
     * @return 락이 적용된 Lecture 엔티티, 없으면 null
     */
    public Lecture findByIdWithLock(Long id) {
        return em.find(Lecture.class, id, LockModeType.NONE);
    }

    /**
     * 지난 3일간의 예약 건수를 기준으로 강연을 조회합니다.
     * 각 강연과 예약 건수(최근 3일간)를 함께 조회하여 인기 순으로 정렬합니다.
     *
     * @param threeDaysAgo 시작 날짜
     * @param now 현재 날짜
     * @return Object 배열 리스트, 각 배열의 0번째 요소는 Lecture, 1번째 요소는 예약 건수(Long)
     */
    public List<Object[]> findPopularLectures(LocalDateTime threeDaysAgo, LocalDateTime now) {
        String jpql = "select l, count(r) as reservationCount " +
                      "from Lecture l " +
                      "left join Reservation r on r.lecture = l and r.createdAt between :threeDaysAgo and :now " +
                      "group by l " +
                      "order by reservationCount desc";
        return em.createQuery(jpql, Object[].class)
                .setParameter("threeDaysAgo", threeDaysAgo)
                .setParameter("now", now)
                .getResultList();
    }
}
