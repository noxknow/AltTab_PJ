package com.ssafy.alttab.study.repository;

import com.ssafy.alttab.study.entity.StudyInfo;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyInfoRepository extends JpaRepository<StudyInfo, Long> {
    List<StudyInfo> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Modifying
    @Query("DELETE FROM StudyInfo s WHERE s.id = :studyId")
    void deleteByStudyId(@Param("studyId") Long studyId);
}