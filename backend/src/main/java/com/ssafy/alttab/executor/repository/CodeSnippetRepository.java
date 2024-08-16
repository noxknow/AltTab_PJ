package com.ssafy.alttab.executor.repository;

import com.ssafy.alttab.executor.entity.CodeSnippet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeSnippetRepository extends JpaRepository<CodeSnippet, Long> {
    Optional<CodeSnippet> findByStudyIdAndProblemIdAndMemberId(Long studyId, Long problemId,
                                                                      Long memberId);
}
