package com.example.jp.controller.Test;

import com.example.jp.ApiResponse;
import com.example.jp.model.Test.Option;
import com.example.jp.model.Test.Question;
import com.example.jp.model.Test.Test;
import com.example.jp.model.Enums.TestType;
import com.example.jp.model.Test.UserTest;
import com.example.jp.model.User;
import com.example.jp.repositories.Test.*;
import com.example.jp.services.CalculateGrade;
import com.example.jp.services.Test.OptionService;
import com.example.jp.services.Test.QuestionService;
import com.example.jp.services.Test.TestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("admin/tests")
public class TestController {

    private final TestService testService;
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final UserTestRepository userTestRepository;
    private final UserAnswerRepository userAnswerRepository;

    @Autowired
    public TestController(TestService testService, TestRepository testRepository, QuestionRepository questionRepository, OptionRepository optionRepository, QuestionService questionService, OptionService optionService, UserTestRepository userTestRepository, UserAnswerRepository userAnswerRepository) {
        this.testService = testService;
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.userTestRepository = userTestRepository;
        this.userAnswerRepository = userAnswerRepository;
    }

    @GetMapping("/{testId}")
    public Test getTestById(@PathVariable Long testId) {
        return testService.getTestByIdWithQuestions(testId);
    }

    @GetMapping
    public List<Test> getTest() {
        return testService.getTest();
    }

    @PutMapping("/{id}/visibility")
    public ResponseEntity<Void> setTestVisibility(@PathVariable Long id, @RequestBody Map<String, Boolean> visibility) {
        boolean newVisibility = visibility.getOrDefault("visibility", false);
        testService.updateVisibility(id, newVisibility);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> editTest(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        testService.editTest(id, updates);
        return ResponseEntity.ok().build();
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public String createTest(@RequestParam("test") String testJson, @RequestParam(value = "images", required = false) List<MultipartFile> imageFiles) {
        // Parse the JSON string into a Map
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> testMap = null;
        try {
            testMap = objectMapper.readValue(testJson, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        String name = (String) testMap.get("name");
        String type = (String) testMap.get("type");
        int timeLimit = (int) testMap.get("timeLimit");
        boolean visibility = (boolean) testMap.get("visibility");
        int maxAttempts = (int) testMap.get("maxAttempts");

        Test test = new Test();
        test.setName(name);
        try {
            test.setType(TestType.valueOf(type.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid test type: " + type);
        }
        test.setVisibility(visibility);
        test.setMaxAttempts(maxAttempts);
        test.setTimeLimit(timeLimit);
        test = testRepository.save(test);

        List<Map<String, Object>> questionsData = (List<Map<String, Object>>) testMap.get("data");
        int imageIndex = 0;
        for (Map<String, Object> questionData : questionsData) {
            String questionText = (String) questionData.get("question");
            List<String> answers = (List<String>) questionData.get("answers");
            List<Integer> correctAnswers = (List<Integer>) questionData.get("correctAnswers");

            Question question = new Question();
            question.setText(questionText);
            question.setTest(test);

            // Save the image if provided
            if (imageFiles != null && !imageFiles.isEmpty() && imageFiles.size() > imageIndex) {
                MultipartFile imageFile = imageFiles.get(imageIndex);
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
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            imageIndex++;
            question = questionRepository.save(question);

            List<Option> options = new ArrayList<>();
            for (int i = 0; i < answers.size(); i++) {
                Option option = new Option();
                option.setText(answers.get(i));
                option.setCorrect(correctAnswers.contains(i));
                option.setQuestion(question);
                options.add(option);
            }
            optionRepository.saveAll(options);
        }
        return test.toString();
    }

    @GetMapping("/result/{testId}")
    public ApiResponse<Map<String, Object>> getResult(@PathVariable Long testId) {
        Map<String, Object> data = new HashMap<>();
        List<UserTest> userTests = userTestRepository.findAllByTestId(testId);

        // Map to store the grouped results
        Map<String, List<Map<String, Object>>> groupedResults = new HashMap<>();

        List<UserTest> sortedUserTests = userTests.stream()
                .sorted(Comparator.comparing(ut -> ut.getUser().getUsername()))
                .toList();

        for (UserTest userTest : sortedUserTests) {
            String username = userTest.getUser().getUsername();
            // Create a map to store individual test results
            Map<String, Object> testResult = new HashMap<>();
            testResult.put("score", userAnswerRepository.calculateTotalPointsByTestId(userTest.getId()));
            testResult.put("maxScore", userTest.getTest().getQuestions().stream().count());
            testResult.put("startDate", userTest.getStartTime());
            long score = userAnswerRepository.calculateTotalPointsByTestId(userTest.getId());
            long maxScore = userTest.getTest().getQuestions().stream().count();
            testResult.put("grade", String.valueOf(CalculateGrade.calculate(score, maxScore)));

            // Add the test result to the list for this user
            groupedResults.computeIfAbsent(username, k -> new ArrayList<>()).add(testResult);
        }

        // Add the grouped results to the response data
        data.put("results", groupedResults);

        return new ApiResponse<>(true, data, "Poprawnie zwrócono wyniki");
    }

}
