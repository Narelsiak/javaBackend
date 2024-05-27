package com.example.jp.services;

import com.example.jp.model.Topics;
import com.example.jp.repositories.TopicsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicsService {

    private final TopicsRepository topicsRepository;

    public TopicsService(TopicsRepository topicsRepository) {
        this.topicsRepository = topicsRepository;
    }

    public List<Topics> getAllTopics() {
        return topicsRepository.findAll();
    }

    public Optional<Topics> getTopicsById(Long id) {
        return topicsRepository.findById(id);
    }

    public Topics createTopics(Topics topics) {
        return topicsRepository.save(topics);
    }

    public Topics updateTopics(Long id, Topics updateTopics) {
        Optional<Topics> existingTopicsOptional = topicsRepository.findById(id);
        if (existingTopicsOptional.isPresent()) {
            Topics existingTopics = existingTopicsOptional.get();
            existingTopics.setName(updateTopics.getName());
            return topicsRepository.save(existingTopics);
        }
        return null;
    }

    public void deleteTopics(Long id) {
        topicsRepository.deleteById(id);
    }
}
