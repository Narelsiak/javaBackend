package com.example.jp.repositories.Topics;

import com.example.jp.model.Topics.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findByTopic_Id(Long topicId);
}
