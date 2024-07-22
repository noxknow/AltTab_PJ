package com.ssafy.alt_tab.drawing.repository;

import com.ssafy.alt_tab.drawing.entity.Drawing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrawingRepository extends JpaRepository<Drawing, Long> {
}
