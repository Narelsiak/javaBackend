package com.example.jp.repositories.Topics;

import com.example.jp.model.Topics.Topics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicsRepository extends JpaRepository<Topics, Long> {
    @Query("SELECT c FROM Topics c WHERE c.sourceCodeList IS NOT EMPTY")
    List<Topics> getAllTopicsWithCode();
    @Query("SELECT c FROM Topics c WHERE c.linkList IS NOT EMPTY")
    List<Topics> getAllTopicsWithLink();
}