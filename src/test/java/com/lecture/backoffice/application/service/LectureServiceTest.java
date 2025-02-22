package com.lecture.backoffice.application.service;

import com.lecture.backoffice.api.dto.LectureRequest;
import com.lecture.backoffice.api.dto.LectureResponse;
import org.springframework.transaction.annotation.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")  // 테스트용 H2 등 사용
@Transactional
public class LectureServiceTest {

    @Autowired
    private LectureService lectureService;

    private LectureRequest validDto;

    @BeforeEach
    void setUp() {
        validDto = new LectureRequest();
        validDto.setLecturer("김정민");
        validDto.setVenue("서울");
        validDto.setCapacity(5);
        validDto.setStartTime(LocalDateTime.of(2025, 2, 20, 17, 0));
        validDto.setEndTime(LocalDateTime.of(2025, 2, 20, 19, 0));
        validDto.setDescription("테스트 강연입니다.");
    }

    @Test
    @DisplayName("정상 강연 등록 테스트")
    void testCreateLectureSuccess() {
        LectureResponse response = lectureService.createLecture(validDto);
        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotNull();
        assertThat(response.getLecturer()).isEqualTo(validDto.getLecturer());
    }

    @Test
    @DisplayName("전체 강연 목록 조회 테스트")
    void testGetAllLectures() {
        int initialCount = lectureService.getAllLectures().size();
        lectureService.createLecture(validDto);
        List<LectureResponse> lectures = lectureService.getAllLectures();
        assertThat(lectures.size()).isEqualTo(initialCount + 1);
    }

    @Test
    @DisplayName("유효하지 않은 입력 테스트 : 종료 시간이 시작 시간보다 빠른 경우")
    void testCreateLectureWithInvalidTime() {
        LectureRequest invalidDto = new LectureRequest();
        invalidDto.setLecturer(validDto.getLecturer());
        invalidDto.setVenue(validDto.getVenue());
        invalidDto.setCapacity(validDto.getCapacity());
        // 시작 시간보다 빠른 종료 시간 설정
        invalidDto.setStartTime(LocalDateTime.of(2025, 2, 20, 17, 0));
        invalidDto.setEndTime(LocalDateTime.of(2025, 2, 20, 16, 0));
        invalidDto.setDescription(validDto.getDescription());

        assertThatThrownBy(() -> lectureService.createLecture(invalidDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("End time must be after start time");
    }
}
