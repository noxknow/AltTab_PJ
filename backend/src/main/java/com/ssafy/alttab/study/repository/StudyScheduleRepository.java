package com.ssafy.alttab.study.repository;

import com.ssafy.alttab.study.entity.StudySchedule;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {
    Optional<StudySchedule> findByStudyIdAndDeadline(Long studyId, LocalDate deadline);

}
