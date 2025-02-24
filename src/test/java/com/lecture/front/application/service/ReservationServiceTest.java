package com.lecture.front.application.service;

import com.lecture.common.domain.model.Lecture;
import com.lecture.front.api.dto.ReservationRequest;
import com.lecture.front.api.dto.ReservationResponse;
import com.lecture.backoffice.domain.repository.LectureRepository;
import com.lecture.front.domain.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ReservationServiceTest {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Lecture lecture;
    private final String employeeNumber = "1234";

    @BeforeEach
    public void setUp() {
        // 강연 용량을 1로 설정해서 한 건만 예약되도록 함.
        lecture = Lecture.builder()
                .lecturer("Test Lecturer")
                .venue("Test Venue")
                .capacity(1)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(2))
                .description("Test Lecture")
                .build();
        lecture = lectureRepository.save(lecture);
    }

    /**
     * 여러 스레드가 동시에 동일 강연/사번으로 예약 요청할 때,
     * 단 하나의 예약만 정상적으로 생성되고, 나머지 요청은 예외가 발생해야 함을 검증합니다.
     */
    @Test
    public void concurrentReservationTest() throws InterruptedException {
        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch startLatch = new CountDownLatch(1);
        List<Future<ReservationResponse>> futures = new ArrayList<>();

        // 스레드마다 예약 요청 작업 생성
        for (int i = 0; i < threadCount; i++) {
            int finalI = i;
            Future<ReservationResponse> future = executor.submit(() -> {
                // 모든 스레드가 동시에 시작되도록 대기
                startLatch.await();
                ReservationRequest request = new ReservationRequest();
                request.setLectureId(2L);
                request.setEmployeeNumber(employeeNumber + Integer.toString(finalI) );
                return reservationService.reserve(request);
            });
            futures.add(future);
        }
        // 스레드 동시에 시작
        startLatch.countDown();
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);

        int successCount = 0;
        int failureCount = 0;
        for (Future<ReservationResponse> future : futures) {
            try {
                ReservationResponse response = future.get();
                if (response != null) {
                    successCount++;
                }
            } catch (ExecutionException e) {
                failureCount++;
            }
        }

        // 강연 용량이 1이므로 단 한 건만 예약되어야 합니다.
        assertEquals(1, successCount);
        assertEquals(threadCount - 1, failureCount);
    }
}
