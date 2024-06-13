package com.example.jp.repositories.Test;

import com.example.jp.model.Test.Question;
import com.example.jp.model.Test.UserTest;
import com.example.jp.model.Test.UserAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, Long> {
    List<UserAnswer> findByUserTest(UserTest userTest);

    @Query("SELECT ua FROM UserAnswer ua WHERE ua.userTest.id = :userTestId")
    List<UserAnswer> findQuestionWithNoAnswers(@Param("userTestId") Long userTestId);

    @Query("SELECT ua FROM UserAnswer ua JOIN ua.userTest ut WHERE ut.id = :testId AND ut.user.id = :userId")
    List<UserAnswer> findByTestIdAndUserId(Long testId, Long userId);

    @Query("SELECT SUM(CASE WHEN ua.isCorrect = true THEN 1 ELSE 0 END) FROM UserAnswer ua WHERE ua.userTest.id = :testId")
    Integer calculateTotalPointsByTestId(Long testId);

    @Query("SELECT ua.question FROM UserAnswer ua WHERE ua.userTest.id = :testId AND ua.userResponse IS NULL")
    Optional<Question> findQuestionWithNoAnswerForTest(Long testId);

    UserAnswer findByUserTestAndQuestion(UserTest userTest, Question question);

}
