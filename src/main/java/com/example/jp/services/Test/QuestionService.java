package com.example.jp.services.Test;

import com.example.jp.model.Test.Option;
import com.example.jp.model.Test.Question;
import com.example.jp.model.Test.Test;
import com.example.jp.repositories.Test.QuestionRepository;
import com.example.jp.repositories.Test.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private TestRepository testRepository;
    public Question getQuestionById(Long questionId) {
        return questionRepository.findById(questionId).orElse(null);
    }

    public Question createQuestion(Question question, Long testId, MultipartFile imageFile) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Test not found"));
        question.setTest(test);

        if (imageFile != null && !imageFile.isEmpty()) {
            String imageName = imageFile.getOriginalFilename();
            String imageExtension = "";
            int dotIndex = imageName.lastIndexOf('.');
            if (dotIndex >= 0) {
                imageExtension = imageName.substring(dotIndex + 1);
            }
            String fileName =  UUID.randomUUID() + "." + imageExtension;
            try {
                Path uploadDir = Paths.get("data/questions");
                if (!Files.exists(uploadDir)) {
                    Files.createDirectories(uploadDir);
                }
                Path filePath = uploadDir.resolve(fileName);
                Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                question.setImagePath(filePath.toString());
            } catch (IOException e) {
                throw new RuntimeException("Error saving image", e);
            }
        }

        for (Option option : question.getOptions()) {
            option.setQuestion(question);
        }

        return questionRepository.save(question);
    }

    public void deleteQuestion(Long questionId) {
        questionRepository.deleteById(questionId);
    }
}
