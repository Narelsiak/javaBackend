package com.example.jp.controller.Test;

import com.example.jp.model.Test.UserTest;
import com.example.jp.model.Test.UserAnswer;
import com.example.jp.services.Test.UserAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user-answers")
public class UserAnswerController {
    @Autowired
    private UserAnswerService userAnswerService;

    @PostMapping("/submit")
    public UserAnswer submitUserAnswer(@RequestBody UserAnswer userAnswer) {
        return userAnswerService.saveUserAnswer(userAnswer);
    }

    @GetMapping("/test/{userTestId}")
    public List<UserAnswer> getUserAnswersByUserTest(@PathVariable Long userTestId) {
        return userAnswerService.getUserAnswersByUserTest(new UserTest(userTestId));
    }
}
