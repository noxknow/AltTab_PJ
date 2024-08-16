package com.ssafy.alttab.study.repository;

import com.ssafy.alttab.study.entity.StudyRollBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudyRollBookRepository extends JpaRepository<StudyRollBook, Long> {
    List<StudyRollBook> findByStudyIdAndTodayDate(Long studyId, LocalDate todayDate);
}
