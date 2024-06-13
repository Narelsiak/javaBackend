package com.example.jp.repositories.Test;

import com.example.jp.model.Test.UserTest;
import com.example.jp.model.Test.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface UserTestRepository extends JpaRepository<UserTest, Long> {
    @Query("SELECT COUNT(ut) FROM UserTest ut WHERE ut.user.id = :userId AND ut.test.id = :testId")
    int countUserAttempts(@Param("userId") Long userId, @Param("testId") Long testId);

    @Query("SELECT ut FROM UserTest ut WHERE ut.user.id = :userId AND ut.isCompleted = false")
    List<UserTest> findIncompleteTestsByUserId(@Param("userId") Long userId);

    Optional<UserTest> findUserTestByIdAndUserId(Long testId, Long userId);
    List<UserTest> findAllByUserId(Long userId);

    List<UserTest> findAllByTestId(Long testId);
}
