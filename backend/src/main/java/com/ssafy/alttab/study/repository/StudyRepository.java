package com.ssafy.alttab.study.repository;

import com.ssafy.alttab.study.entity.Study;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<Study, Long> {
    List<Study> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}