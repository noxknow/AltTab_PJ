//package com.ssafy.alttab.common.jointable.repository;
//
//import com.ssafy.alttab.common.jointable.entity.MemberStudy;
//import com.ssafy.alttab.member.entity.Member;
//import com.ssafy.alttab.member.enums.MemberRoleStatus;
//import com.ssafy.alttab.study.entity.StudyInfo;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//import java.util.Optional;
//
//@Repository
//public interface MemberStudyRepository extends JpaRepository<MemberStudy, Long> {
//    @Query("SELECT ms FROM MemberStudy ms WHERE ms.member.username = :username AND ms.studyInfo.id = :studyId")
//    Optional<MemberStudy> findByUsernameAndStudyId(@Param("username") String username, @Param("studyId") Long studyId);
//
//    @Query("SELECT ms FROM MemberStudy ms WHERE ms.member.memberId = :memberId AND ms.studyInfo.id = :studyId")
//    Optional<MemberStudy> findByMemberIdAndStudyId(@Param("memberId") Long memberId, @Param("studyId") Long studyId);
//
//    boolean existsByMemberAndStudyInfo(Member member, StudyInfo studyInfo);
//
//
//    @Modifying
//    @Query("DELETE FROM MemberStudy ms WHERE ms.member.username  = :username AND ms.studyInfo.id = :studyId AND ms.role = :role")
//    void deleteByUsernameAndStudyId(@Param("username ") String username, @Param("studyId") Long studyId, @Param("role") MemberRoleStatus role);
//
//    @Query("SELECT ms FROM MemberStudy ms WHERE ms.member.memberId = :memberId AND ms.studyInfo.id = :studyId AND ms.role = :role")
//    Optional<MemberStudy> findByMemberIdAndStudyIdAndRole(@Param("memberId") Long memberId, @Param("studyId") Long studyId, @Param("role") MemberRoleStatus role);
//}