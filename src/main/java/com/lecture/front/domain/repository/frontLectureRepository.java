package com.lecture.front.domain.repository;

import com.lecture.common.domain.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Lecture 엔티티의 CRUD 작업을 수행하는 Repository.
 * 공통 도메인 Lecture 엔티티를 사용하므로, JPQL 쿼리에서 엔티티 이름은 "Lecture"를 사용합니다.
 */
@Repository
public interface frontLectureRepository extends JpaRepository<Lecture, Long> {
    List<Lecture> findByStartTimeBetween(LocalDateTime from, LocalDateTime to);
}
