package com.ssafy.alttab.study.repository;

import com.ssafy.alttab.study.entity.Study;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    List<Study> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COUNT(s) + 1 FROM Study s WHERE s.studyPoint > (SELECT s2.studyPoint FROM Study s2 WHERE s2.id = :studyId)")
    int findStudyPointRankById(@Param("studyId") Long studyId);
}