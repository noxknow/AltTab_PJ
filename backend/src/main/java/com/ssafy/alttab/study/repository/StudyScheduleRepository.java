package com.ssafy.alttab.study.repository;

import com.ssafy.alttab.study.entity.StudySchedule;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyScheduleRepository extends JpaRepository<StudySchedule, Long> {

    @EntityGraph(attributePaths = {"ScheduleProblems", "ScheduleProblems.problem"})
    Optional<StudySchedule> findByStudyIdAndDeadline(Long studyId, LocalDate deadline);

    List<StudySchedule> findAllByStudyIdAndDeadlineBetween(Long studyId, LocalDate startDate, LocalDate endDate);

    Optional<StudySchedule> findFirstByStudyIdAndDeadlineGreaterThanEqualOrderByDeadlineAsc(Long studyId, LocalDate date);
}
