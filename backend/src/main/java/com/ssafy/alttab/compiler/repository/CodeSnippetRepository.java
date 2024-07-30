package com.ssafy.alttab.compiler.repository;

import com.ssafy.alttab.compiler.entity.CodeSnippet;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeSnippetRepository extends JpaRepository<CodeSnippet, Long> {
    Optional<CodeSnippet> findById(Long id);
}
