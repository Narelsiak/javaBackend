package com.example.jp.repositories.Topics;

import com.example.jp.model.Topics.SourceCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SourceCodeRepository extends JpaRepository<SourceCode, Long> {
    List<SourceCode> findByTopicId(Long topicsId);
}
