package com.example.jp.repositories;

import com.example.jp.model.Topics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicsRepository extends JpaRepository<Topics, Long> {
}