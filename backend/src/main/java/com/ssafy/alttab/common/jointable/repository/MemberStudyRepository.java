package com.ssafy.alttab.common.jointable.repository;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.study.entity.StudyInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberStudyRepository extends JpaRepository<MemberStudy, Long> {
    List<MemberStudy> findByMember(Member member);

    List<MemberStudy> findByStudyInfo(StudyInfo studyInfo);

    Optional<MemberStudy> findByMemberAndStudyInfo(Member member, StudyInfo studyInfo);

    boolean existsByMemberAndStudyInfo(Member member, StudyInfo studyInfo);
}