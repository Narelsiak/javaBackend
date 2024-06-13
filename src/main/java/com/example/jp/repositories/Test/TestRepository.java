package com.example.jp.repositories.Test;

import com.example.jp.model.Test.Question;
import com.example.jp.model.Test.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    @Query("SELECT t FROM Test t LEFT JOIN FETCH t.questions q WHERE t.id = :id")
    Test findByIdWithQuestions(Long id);
    @Query("SELECT t FROM Test t WHERE t.visibility = true")
    List<Test> findAllVisibility();
}
