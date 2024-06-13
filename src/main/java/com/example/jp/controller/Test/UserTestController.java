package com.example.jp.controller.Test;

import com.example.jp.ApiResponse;
import com.example.jp.SecurityUtils;
import com.example.jp.model.Test.Question;
import com.example.jp.model.Test.Test;
import com.example.jp.repositories.Test.TestRepository;
import com.example.jp.services.Test.TestService;
import com.example.jp.services.Test.UserTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/user-tests")
public class UserTestController {

    @Autowired
    private UserTestService userTestService;
    @Autowired
    private TestService testService;
    @Autowired
    private TestRepository testRepository;

    @PostMapping("/start")
    public ApiResponse<Map<String, Object>> startTest(@RequestParam Long testId) {
        return userTestService.startTest(SecurityUtils.getCurrentUsername(), testId);
    }

    @PostMapping("/answer")
    public ApiResponse<Map<String, Object>> answerQuestion(@RequestParam Long questionId, @RequestParam String selectedOptionIds, @RequestParam Long testId) {
        List<Long> selectedOptions = Arrays.stream(selectedOptionIds.split(","))
                .map(Long::parseLong)
                .toList();
        return userTestService.saveAnswer(SecurityUtils.getCurrentUsername(), questionId, selectedOptions, testId);
    }
    @PostMapping("/continue")
    public ApiResponse<Map<String, Object>> continueTest(@RequestParam Long testId) {
        return userTestService.continueTest(SecurityUtils.getCurrentUsername(), testId);
    }
    @GetMapping
    public ApiResponse<Map<String, Object>> testList() {
        return userTestService.testList(SecurityUtils.getCurrentUsername());
    }
    @GetMapping("/list")
    public List<Map<String, Object>> testToSolveList() {
        List<Test> list = testRepository.findAllVisibility();
        List<Map<String, Object>> resultList = new ArrayList<>();

        for (Test test : list) {
            List<Question> questions = test.getQuestions();
            int questionsCount = questions != null ? questions.size() : 0;

            Map<String, Object> testMap = new HashMap<>();
            testMap.put("id", test.getId());
            testMap.put("name", test.getName());
            testMap.put("type", test.getType());
            testMap.put("maxAttempts", test.getMaxAttempts());
            testMap.put("timeLimit", test.getTimeLimit());
            testMap.put("questionsCount", questionsCount);

            resultList.add(testMap);
        }

        return resultList;
    }

    @GetMapping("/result")
    public ApiResponse<Map<String, Object>> testResult(@RequestParam Long testId) {
        String username = SecurityUtils.getCurrentUsername();
        return userTestService.getTestResult(username, testId);
    }
}
