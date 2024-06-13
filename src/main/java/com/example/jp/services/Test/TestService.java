package com.example.jp.services.Test;

import com.example.jp.model.Enums.TestType;
import com.example.jp.model.Test.Question;
import com.example.jp.model.Test.Test;
import com.example.jp.repositories.Test.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class TestService {
    @Autowired
    private TestRepository testRepository;

    public List<Test> getTest(){
        List <Test> tests = testRepository.findAll();
        for(Test test : tests){
            test.setQuestions(null);
        }
        return tests;
    }
    public Test createTest(Test test) {
        return testRepository.save(test);
    }
    public Test getTestByIdWithQuestions(Long id) {
        return testRepository.findByIdWithQuestions(id);
    }
    public void updateVisibility(Long id, boolean visibility) {
        Optional<Test> testOptional = testRepository.findById(id);
        if (testOptional.isPresent()) {
            Test test = testOptional.get();
            test.setVisibility(visibility);
            testRepository.save(test);
        }
    }

    public void editTest(Long id, Map<String, Object> updates) {
        Optional<Test> existingTestOptional = testRepository.findById(id);
        if (existingTestOptional.isPresent()) {
            Test existingTest = existingTestOptional.get();

            if (updates.containsKey("name")) {
                existingTest.setName((String) updates.get("name"));
            }
            if (updates.containsKey("type")) {
                String typeString = (String) updates.get("type");
                try {
                    TestType testType = TestType.valueOf(typeString);
                    existingTest.setType(testType);
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException("Invalid test type: " + typeString);
                }
            }
            if (updates.containsKey("maxAttempts")) {
                String maxAttempts = (String) updates.get("maxAttempts");
                existingTest.setMaxAttempts(Integer.parseInt(maxAttempts));
            }
            if (updates.containsKey("randomQuestion")) {
                String randomQuestion = (String) updates.get("randomQuestion");
                if(!randomQuestion.isEmpty()){
                    existingTest.setRandomQuestion(true);
                }
            }else{
                existingTest.setRandomQuestion(false);
            }
            if (updates.containsKey("timeLimit")) {
                String timeLimit = (String) updates.get("timeLimit");
                existingTest.setTimeLimit(Integer.parseInt(timeLimit));
            }
            testRepository.save(existingTest);
        }
    }
}
