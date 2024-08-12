package com.ssafy.alttab.common.jointable.repository;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import com.ssafy.alttab.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberStudyRepository extends JpaRepository<MemberStudy, Long> {

    Optional<MemberStudy> findMemberByStudyIdAndRole(Long studyId, MemberRoleStatus role);

    List<MemberStudy> findByMember(Member member);

    List<MemberStudy> findByMemberName(String name);

    Optional<MemberStudy> findByMemberAndStudyAndRole(Member member, Study study, MemberRoleStatus role);

}