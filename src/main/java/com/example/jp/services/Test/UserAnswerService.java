package com.example.jp.services.Test;

import com.example.jp.model.Test.UserTest;
import com.example.jp.repositories.Test.UserAnswerRepository;
import com.example.jp.model.Test.UserAnswer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAnswerService {
    @Autowired
    private UserAnswerRepository userAnswerRepository;

    public UserAnswer saveUserAnswer(UserAnswer userAnswer) {
        return userAnswerRepository.save(userAnswer);
    }

    public List<UserAnswer> getUserAnswersByUserTest(UserTest userTest) {
        return userAnswerRepository.findByUserTest(userTest);
    }

    // Inne metody serwisu w razie potrzeby
}
