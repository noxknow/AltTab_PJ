package com.ssafy.alttab.member.repository;

import com.ssafy.alttab.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    Optional<Member> findByMemberEmail(String MemberEmail);

    List<Member> findAllByMemberEmailIn(List<String> memberEmails);
}
