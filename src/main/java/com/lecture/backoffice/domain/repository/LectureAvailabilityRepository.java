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
}
