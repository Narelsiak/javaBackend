package com.example.jp.controller.TopicsController;

import com.example.jp.model.Topics;
import com.example.jp.services.TopicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/topics")
public class TopicsController {

    private final TopicsService topicsService;

    @Autowired
    public TopicsController(TopicsService topicsService) {
        this.topicsService = topicsService;
    }

    @GetMapping
    public List<Topics> getAllTopics() {
        List<Topics> topics = topicsService.getAllTopics();
        return topicsService.getAllTopics();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTopicsById(@PathVariable Long id) {
        Optional<Topics> topicsOptional = topicsService.getTopicsById(id);
        if (topicsOptional.isPresent()) {
            Topics topics = topicsOptional.get();
            return new ResponseEntity<>(topics, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Topics not found", HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<Topics> addTopics(@RequestParam("name") String name) {
        Topics topics = new Topics();
        topics.setName(name);
        topicsService.createTopics(topics);
        return new ResponseEntity<>(topics, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Topics> updateTopics(@PathVariable Long id, @RequestBody Topics topics) {
        Topics updateTopics = topicsService.updateTopics(id, topics);
        return new ResponseEntity<>(updateTopics, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTopics(@PathVariable Long id) {
        try {
            topicsService.deleteTopics(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Wystąpił błąd: " + e.getMessage());
        }
    }
}
