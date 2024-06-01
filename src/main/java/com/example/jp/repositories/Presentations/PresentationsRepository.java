package com.example.jp.repositories.Presentations;

import com.example.jp.model.Presentations.Presentations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PresentationsRepository extends JpaRepository<Presentations, Long> {
    List<Presentations> findByCategoryId(Long categoryId);
}