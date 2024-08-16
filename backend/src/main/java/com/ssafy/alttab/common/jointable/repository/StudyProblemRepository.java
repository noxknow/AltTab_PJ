package com.ssafy.alttab.common.jointable.repository;

import com.ssafy.alttab.common.jointable.entity.StudyProblem;
import com.ssafy.alttab.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface StudyProblemRepository extends JpaRepository<StudyProblem, Long> {
    List<StudyProblem> findByStudyAndTag(Study study, String tag);

    List<StudyProblem> findByStudyAndLevel(Study study, Long level);

    List<StudyProblem> findByStudyAndPresenter(Study study, String presenter);

    List<StudyProblem> findByStudyAndDeadlineGreaterThanEqualOrderByDeadlineAsc(Study study, LocalDate deadline);

    List<StudyProblem> findByStudyOrderByDeadlineDesc(Study study);

    List<StudyProblem> findByStudyId(Long studyId);

    StudyProblem findByStudyIdAndProblemProblemIdAndDeadline(Long studyId, Long problemId, LocalDate deadline);

    @Transactional
    @Query("DELETE FROM StudyProblem sp WHERE sp.study.id = :studyId AND sp.deadline = :deadline")
    void deleteAllByStudyIdAndDeadline(@Param("studyId") Long studyId, @Param("deadline") LocalDate deadline);

    @Query("SELECT sp FROM StudyProblem sp WHERE sp.study = :study AND sp.problem.title LIKE %:title%")
    List<StudyProblem> findByStudyAndProblemTitleContaining(@Param("study") Study study, @Param("title") String title);

    @Query("SELECT sp FROM StudyProblem sp WHERE sp.study = :study AND sp.problem.tag LIKE %:tag%")
    List<StudyProblem> findByStudyAndProblemTagContaining(@Param("study") Study study, @Param("tag") String tag);
}