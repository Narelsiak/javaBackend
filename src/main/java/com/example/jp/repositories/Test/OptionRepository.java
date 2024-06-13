package com.example.jp.repositories.Test;

import com.example.jp.model.Test.Option;
import com.example.jp.model.Test.Question;
import com.example.jp.model.Test.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionRepository extends JpaRepository<Option, Long> {
    @Query("SELECT o FROM Option o WHERE o.question.id = :questionId AND o.correct = true")
    List<Option> findCorrectOptions(@Param("questionId") Long questionId);

    List<Option> findByQuestionId(Long questionId);
}
