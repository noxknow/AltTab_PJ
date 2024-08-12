package com.ssafy.alttab.study.repository;

import com.ssafy.alttab.study.entity.StudySchedule;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {
    Optional<StudySchedule> findByIdAndStartDay(Long id, LocalDateTime startDate);
}
