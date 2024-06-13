package com.example.jp.controller.Test;

import com.example.jp.model.Test.Option;
import com.example.jp.model.Test.Question;
import com.example.jp.repositories.Test.QuestionRepository;
import com.example.jp.services.Test.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/questions")
public class QuestionController {
    @Autowired
    private QuestionService questionService;
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/{questionId}")
    public Question getQuestionById(@PathVariable Long questionId) {
         Question question = questionService.getQuestionById(questionId);
         //System.out.println(question);
         return question;
    }

    @PostMapping("/create/{testId}")
    public Question createQuestion(@PathVariable Long testId, @RequestBody Question question) {
        return questionService.createQuestion(question, testId);
    }

    @PutMapping("/{questionId}")
    public ResponseEntity<String> updateQuestion(@PathVariable Long questionId, @RequestBody Question question) {
        Question existingQuestion = questionRepository.findById(questionId).orElse(null);
        if (existingQuestion == null) {
            return ResponseEntity.notFound().build();
        }

        existingQuestion.setText(question.getText());

        List<Long> newOptionIds = question.getOptions().stream()
                .filter(option -> option.getId() != null)
                .map(Option::getId)
                .collect(Collectors.toList());

        // Usuń opcje, które nie są już obecne
        existingQuestion.getOptions().removeIf(existingOption ->
                !newOptionIds.contains(existingOption.getId())
        );

        // Aktualizuj istniejące opcje i dodaj nowe opcje
        for (Option option : question.getOptions()) {
            if (option.getId() != null) {
                existingQuestion.getOptions().stream()
                        .filter(existingOption -> existingOption.getId().equals(option.getId()))
                        .findFirst()
                        .ifPresent(existingOption -> {
                            existingOption.setText(option.getText());
                            existingOption.setCorrect(option.isCorrect());
                        });
            } else {
                option.setQuestion(existingQuestion);
                existingQuestion.getOptions().add(option);
            }
        }

        questionRepository.save(existingQuestion);

        return ResponseEntity.ok("Pytanie zostało pomyślnie zaktualizowane.");
    }


}
