package com.example.jp.services.Test;

import com.example.jp.model.Test.Option;
import com.example.jp.model.Test.Question;
import com.example.jp.model.Test.Test;
import com.example.jp.repositories.Test.QuestionRepository;
import com.example.jp.repositories.Test.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;
    public Question getQuestionById(Long questionId) {
        return questionRepository.findById(questionId).orElse(null);
    }

    public Question createQuestion(Question question, Long testId) {
        System.out.println(question);
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found"));
        question.setTest(test);
        for (Option option : question.getOptions()) {
            option.setQuestion(question);
        }
        return questionRepository.save(question);
    }
}
