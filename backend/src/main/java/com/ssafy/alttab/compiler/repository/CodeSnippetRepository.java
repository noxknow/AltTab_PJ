package com.ssafy.alttab.compiler.repository;

import com.ssafy.alttab.compiler.entity.CodeSnippet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeSnippetRepository extends JpaRepository<CodeSnippet, Long> {
}
