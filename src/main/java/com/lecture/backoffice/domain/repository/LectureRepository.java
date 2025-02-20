package com.lecture.backoffice.domain.repository;

import com.lecture.common.domain.model.Lecture;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Lecture 엔티티의 CRUD 작업을 위한 Repository
 */
@Repository("backofficeLectureRepository")
public class LectureRepository {

    @PersistenceContext
    private EntityManager em;

    // 강연 저장: id가 null이면 persist, 아니면 merge
    public Lecture save(Lecture lecture) {
        if (lecture.getId() == null) {
            em.persist(lecture);
            return lecture;
        } else {
            return em.merge(lecture);
        }
    }

    // 모든 강연 조회
    public List<Lecture> findAll() {
        return em.createQuery("select l from Lecture l", Lecture.class).getResultList();
    }
}
