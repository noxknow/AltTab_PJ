package com.ssafy.alt_tab.compiler.repository;

import com.ssafy.alt_tab.compiler.entity.CodeSnippet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeSnippetRepository extends JpaRepository<CodeSnippet, Long> {
}
