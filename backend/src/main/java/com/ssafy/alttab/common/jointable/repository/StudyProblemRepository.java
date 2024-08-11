package com.ssafy.alttab.common.jointable.repository;

import com.ssafy.alttab.common.jointable.entity.StudyProblem;
import com.ssafy.alttab.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface StudyProblemRepository extends JpaRepository<StudyProblem, Long> {
    List<StudyProblem> findByStudyAndTag(Study study, String tag);

    List<StudyProblem> findByStudyAndLevel(Study study, Long level);

    List<StudyProblem> findByStudyAndPresenter(Study study, String presenter);

    List<StudyProblem> findByStudyAndDeadlineGreaterThanEqualOrderByDeadlineAsc(Study study, LocalDate deadline);

    List<StudyProblem> findByStudyOrderByDeadlineDesc(Study study);

    void deleteByStudyIdAndIdIn(Long studyId, List<Long> problemIds);
}
