package com.lecture.front.domain.repository;

import com.lecture.common.domain.model.Lecture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Lecture 엔티티의 CRUD 작업을 수행하는 Repository.
 * 공통 도메인 Lecture 엔티티를 사용하므로, JPQL 쿼리에서 엔티티 이름은 "Lecture"를 사용합니다.
 */
@Repository("frontLectureRepository")
public class LectureRepository {

    @PersistenceContext
    private EntityManager em;

    /**
     * ID로 Lecture 엔티티 조회
     *
     * @param id 강연 ID
     * @return Lecture 엔티티, 없으면 null
     */
    public Lecture findById(Long id) {
        return em.find(Lecture.class, id);
    }

    /**
     * 강연을 조회할 때 PESSIMISTIC_WRITE 락을 적용하여 동시성 문제를 완화합니다.
     *
     * @param id 강연 ID
     * @return 락이 적용된 Lecture 엔티티, 없으면 null
     */
    public Lecture findByIdWithLock(Long id) {
        return em.find(Lecture.class, id, LockModeType.PESSIMISTIC_WRITE);
    }

    /**
     * 모든 강연을 조회합니다.
     * 공통 도메인 Lecture 엔티티를 사용하므로 JPQL 쿼리에서 엔티티 이름은 "Lecture"를 사용합니다.
     *
     * @return Lecture 엔티티 리스트
     */
    public List<Lecture> findAll() {
        return em.createQuery("select l from Lecture l", Lecture.class)
                .getResultList();
    }
}
