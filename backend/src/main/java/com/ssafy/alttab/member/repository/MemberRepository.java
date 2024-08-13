package com.ssafy.alttab.member.repository;

import com.ssafy.alttab.member.entity.Member;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByName(String name);

    List<Member> findByNameContaining(String name);
}
