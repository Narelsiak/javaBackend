package com.example.jp.services.Test;

import com.example.jp.ApiResponse;
import com.example.jp.model.Enums.TestType;
import com.example.jp.model.Test.*;
import com.example.jp.model.User;
import com.example.jp.repositories.Test.*;
import com.example.jp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserTestService {

    @Autowired
    private UserTestRepository userTestRepository;

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private UserAnswerRepository userAnswerRepository;
    @Autowired
    private OptionRepository optionRepository;

    public ApiResponse<Map<String, Object>> startTest(String userNickname, Long testId) {
        User user = userRepository.findUserByNickname(userNickname);
        Test test = testRepository.findById(testId).orElseThrow(() -> new RuntimeException("Test not found"));

        List<UserTest> incompleteTests = userTestRepository.findIncompleteTestsByUserId(user.getId());
        for (UserTest userTest : incompleteTests) {
            LocalDateTime endTime = userTest.getStartTime().plusMinutes(userTest.getTest().getTimeLimit());
            System.out.println(endTime);
            if (LocalDateTime.now().isAfter(endTime)) {
                userTest.setCompleted(true);
                userTestRepository.save(userTest);
            } else {
                throw new RuntimeException("You have an ongoing test that is not yet completed.");
            }
        }

        int currentAttempts = userTestRepository.countUserAttempts(user.getId(), testId);
        if (currentAttempts >= test.getMaxAttempts() && test.getType() != TestType.QUIZ) {
            throw new RuntimeException("Max attempts reached for this test");
        }

        UserTest userTest = new UserTest();
        userTest.setUser(user);
        userTest.setTest(test);
        userTest.setStartTime(LocalDateTime.now());
        userTest.setCompleted(false);

        userTestRepository.save(userTest);
        Question question;
        if(test.isRandomQuestion()){
            List<Question> questions = questionRepository.findByTestId(testId);
            Collections.shuffle(questions);
            question = questions.getFirst();
        }else{
            System.out.println(testId);
            question = questionRepository.findByTestId(testId).getFirst();
        }

        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setUserTest(userTest);
        userAnswer.setQuestion(question);
        userAnswerRepository.save(userAnswer);

        Map<String, Object> data = new HashMap<>();

        test.setQuestions(null);
        test.setId(userTest.getId());
        data.put("test", test);
        data.put("question", question);

        return new ApiResponse<>(true, data, "ok");
    }

    public ApiResponse<Map<String, Object>> saveAnswer(String userNickname, Long questionId, List<Long> selectedOptionIds, Long testId) {
        Map<String, Object> data = new HashMap<>();
        User user = userRepository.findUserByNickname(userNickname);
        if (user == null) {
            return new ApiResponse<>(false, null, "User not found");
        }

        Optional<UserTest> optionalTest = userTestRepository.findIncompleteTestsByUserId(user.getId()).stream().findFirst();
        if (!optionalTest.isPresent()) {
            return new ApiResponse<>(false, null, "Incomplete test not found");
        }

        UserTest test = optionalTest.get();
        LocalDateTime endTime = test.getStartTime().plusMinutes(test.getTest().getTimeLimit());
        if(LocalDateTime.now().isAfter(endTime)){
            test.setCompleted(true);
            test.setEndTime(endTime);
            userTestRepository.save(test);
            data.put("score", userAnswerRepository.calculateTotalPointsByTestId(testId));
            return new ApiResponse<>(false, data, "Time's up");
        }
        if (!test.getId().equals(testId)) {
            return new ApiResponse<>(false, null, "Test ID does not match");
        }

        Optional<UserAnswer> optionalAnswer = userAnswerRepository.findQuestionWithNoAnswers(testId).stream().reduce((first, second) -> second);
        if (!optionalAnswer.isPresent()) {
            return new ApiResponse<>(false, null, "No unanswered questions found");
        }

        UserAnswer answer = optionalAnswer.get();
        if (answer.getUserResponse() != null || !answer.getQuestion().getId().equals(questionId)) {
            return new ApiResponse<>(false, null, "Invalid question or already answered");
        }

        answer.setUserResponse(selectedOptionIds.toString());

        List<Option> correctOptions = optionRepository.findCorrectOptions(questionId);
        List<Long> correctOptionIds = correctOptions.stream()
                .map(Option::getId)
                .collect(Collectors.toList());

        boolean isCorrect = correctOptionIds.containsAll(selectedOptionIds) && selectedOptionIds.containsAll(correctOptionIds);
        answer.setIsCorrect(isCorrect);

        userAnswerRepository.save(answer);
        List<Question> allQuestions = questionRepository.findByTestId(test.getTest().getId());
        List<UserAnswer> userAnswers = userAnswerRepository.findByTestIdAndUserId(testId, user.getId());
        List<Question> answeredQuestions = userAnswers.stream()
                .map(UserAnswer::getQuestion)
                .collect(Collectors.toList());
        allQuestions.removeAll(answeredQuestions);
        if (allQuestions.isEmpty()) {
            test.setCompleted(true);
            test.setEndTime(LocalDateTime.now());
            userTestRepository.save(test);
            int score = userAnswerRepository.calculateTotalPointsByTestId(testId);
            data.put("score", score);
            return new ApiResponse<>(false, data, "No more questions available");
        }

        if(test.getTest().isRandomQuestion()){
            Collections.shuffle(allQuestions);
        }
        Question nextQuestion = allQuestions.get(0);

        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setUserTest(userTestRepository.findById(testId).get());
        userAnswer.setQuestion(nextQuestion);
        userAnswerRepository.save(userAnswer);

        data.put("question", nextQuestion);
        data.put("testName", test.getTest().getName());
        data.put("testId", test.getId());
        LocalDateTime currentTime = LocalDateTime.now();
        long minutesRemaining = ChronoUnit.MINUTES.between(currentTime, test.getStartTime().plusMinutes(test.getTest().getTimeLimit()));
        data.put("endTime", minutesRemaining);

        return new ApiResponse<>(true, data, "Answer saved successfully");
    }

    public ApiResponse<Map<String, Object>> continueTest(String userNickname, Long testId) {
        Map<String, Object> data = new HashMap<>();
        User user = userRepository.findUserByNickname(userNickname);
        if (user == null) {
            return new ApiResponse<>(false, null, "User not found");
        }
        Optional<UserTest> optionalUserTest = userTestRepository.findUserTestByIdAndUserId(testId, user.getId());
        if (!optionalUserTest.isPresent()) {
            return new ApiResponse<>(false, null, "Test not found for the user");
        }

        UserTest userTest = optionalUserTest.get();
        LocalDateTime endTime = userTest.getStartTime().plusMinutes(userTest.getTest().getTimeLimit());
        if(LocalDateTime.now().isAfter(endTime)){
            userTest.setCompleted(true);
            userTest.setEndTime(endTime);
            userTestRepository.save(userTest);
            data.put("score", userAnswerRepository.calculateTotalPointsByTestId(testId));
            return new ApiResponse<>(false, data, "Test time expired");
        }
        Optional<Question> optionalQuestion = userAnswerRepository.findQuestionWithNoAnswerForTest(testId);

        if (!optionalQuestion.isPresent()) {
            return new ApiResponse<>(false, null, "No question available");
        }
        Question currentQuestion = optionalQuestion.get();

        LocalDateTime currentTime = LocalDateTime.now();
        long minutesRemaining = ChronoUnit.MINUTES.between(currentTime, userTest.getStartTime().plusMinutes(userTest.getTest().getTimeLimit()));

        data.put("endTime", minutesRemaining);
        data.put("testName", userTest.getTest().getName());
        data.put("testId", userTest.getId());
        data.put("question", currentQuestion);
        return new ApiResponse<>(true, data, "Continue test");
    }

    public ApiResponse<Map<String, Object>> testList(String userNickname) {
        Map<String, Object> data = new HashMap<>();

        Long id = userRepository.findUserByNickname(userNickname).getId();
        if (id == null) {
            return new ApiResponse<>(false, null, "User not found");
        }

        List<UserTest> userTests = userTestRepository.findAllByUserId(id);
        if (userTests.isEmpty()) {
            return new ApiResponse<>(false, null, "No tests found for the user");
        }

        List<Map> unfinishedTests = new ArrayList<>();
        List<Map> finishedTests = new ArrayList<>();

        for (UserTest userTest : userTests) {
            Map<String, Object> test = new HashMap<>();
            String testName = userTest.getTest().getName();
            boolean isCompleted = userTest.isCompleted();
            LocalDateTime date = userTest.getStartTime();
            int time = userTest.getTest().getTimeLimit();
            test.put("name", testName);
            test.put("isCompleted", isCompleted);
            test.put("date", date);
            test.put("time", time);
            test.put("type", userTest.getTest().getType());
            if (userTest.isCompleted()) {
                test.put("id", userTest.getId());
                test.put("score", userAnswerRepository.calculateTotalPointsByTestId(userTest.getId()));
                System.out.println(userTest.getId());
                finishedTests.add(test);
            } else {
                test.put("id", userTest.getId());
                unfinishedTests.add(test);
            }
        }
        data.put("unfinished", unfinishedTests);
        data.put("finished", finishedTests);

        return new ApiResponse<>(true, data, "Test list retrieved successfully");
    }

    public ApiResponse<Map<String, Object>> getTestResult(String username, Long testId) {
        User user = userRepository.findUserByNickname(username);
        if (user == null) {
            return new ApiResponse<>(false, null, "User not found");
        }

        Optional<UserTest> optionalUserTest = userTestRepository.findUserTestByIdAndUserId(testId, user.getId());
        if (!optionalUserTest.isPresent()) {
            return new ApiResponse<>(false, null, "Test not found for the user");
        }

        UserTest userTest = optionalUserTest.get();
        Test test = userTest.getTest();

        if (test.getType() == TestType.EXAM) {
            return new ApiResponse<>(false, null, "Access to results is restricted for EXAM type tests");
        }

        Map<String, Object> data = new HashMap<>();
        List<Map<String, Object>> questionResults = new ArrayList<>();

        for (Question question : test.getQuestions()) {
            Map<String, Object> questionResult = new HashMap<>();
            questionResult.put("id", question.getId());
            questionResult.put("text", question.getText());

            List<Map<String, Object>> options = new ArrayList<>();
            for (Option option : question.getOptions()) {
                Map<String, Object> optionResult = new HashMap<>();
                optionResult.put("id", option.getId());
                optionResult.put("text", option.getText());
                optionResult.put("isCorrect", option.isCorrect());
                options.add(optionResult);
            }
            questionResult.put("options", options);

            UserAnswer userAnswer = userAnswerRepository.findByUserTestAndQuestion(userTest, question);
            List<Long>selectedOptionIds = new ArrayList<>();
            if(userAnswer != null) {
                selectedOptionIds = parseSelectedOptionIds(userAnswer.getUserResponse());
                questionResult.put("correct", userAnswer.getIsCorrect());
            }
            questionResult.put("selectedOptionIds", selectedOptionIds);

            questionResults.add(questionResult);
        }
        data.put("score", userAnswerRepository.calculateTotalPointsByTestId(userTest.getId()));
        data.put("totalScore", userTest.getTest().getQuestions().toArray().length);
        data.put("questions", questionResults);
        return new ApiResponse<>(true, data, "Test result retrieved successfully");
    }

    private List<Long> parseSelectedOptionIds(String userResponse) {
        if (userResponse == null || userResponse.isEmpty()) {
            return new ArrayList<>();
        }

        // Remove any unwanted characters such as [ and ]
        userResponse = userResponse.replaceAll("[\\[\\]]", "");

        return Arrays.stream(userResponse.split(","))
                .map(String::trim)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
