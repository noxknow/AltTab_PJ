package com.ssafy.alttab.common.jointable.repository;

import com.ssafy.alttab.common.jointable.entity.MemberStudy;
import com.ssafy.alttab.member.entity.Member;
import com.ssafy.alttab.member.enums.MemberRoleStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberStudyRepository extends JpaRepository<MemberStudy, Long> {

    Optional<MemberStudy> findByMember_NameAndStudy_IdAndRole(String name, Long study_Id, MemberRoleStatus role);

    Optional<MemberStudy> findMemberByStudyIdAndRole(Long studyId, MemberRoleStatus role);
}