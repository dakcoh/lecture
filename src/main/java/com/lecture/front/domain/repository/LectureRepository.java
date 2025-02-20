package com.lecture.front.domain.repository;

import com.lecture.front.domain.model.Lecture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.LockModeType;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Lecture 엔티티의 CRUD 작업을 수행하는 Repository
 */
@Repository("frontLectureRepository")
public class LectureRepository {

    @PersistenceContext
    private EntityManager em;

    public Lecture save(Lecture lecture) {
        if (lecture.getId() == null) {
            em.persist(lecture);
            return lecture;
        } else {
            return em.merge(lecture);
        }
    }

    public Lecture findById(Long id) {
        return em.find(Lecture.class, id);
    }

    /**
     * 강연을 조회할 때 PESSIMISTIC_WRITE 락을 적용하여 동시성 문제를 최소화합니다.
     *
     * @param id 강연 ID
     * @return 락이 적용된 Lecture 엔티티
     */
    public Lecture findByIdWithLock(Long id) {
        return em.find(Lecture.class, id, LockModeType.PESSIMISTIC_WRITE);
    }

    public List<Lecture> findAll() {
        return em.createQuery("select l from frontLecture l", Lecture.class).getResultList();
    }
}
