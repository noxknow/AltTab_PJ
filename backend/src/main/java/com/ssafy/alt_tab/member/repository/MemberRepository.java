package com.ssafy.alt_tab.member.repository;

import com.ssafy.alt_tab.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByUsername(String username);
}
