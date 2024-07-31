package com.ssafy.alttab.study.repository;

import com.ssafy.alttab.study.entity.StudyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyInfoRepository extends JpaRepository<StudyInfo, Long> {
}