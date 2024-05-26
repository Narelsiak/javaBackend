package com.example.jp.repositories;

import com.example.jp.model.Presentations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PresentationsRepository extends JpaRepository<Presentations, Long> {
}