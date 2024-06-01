package com.example.jp.services.Topics;

import com.example.jp.model.Topics.SourceCode;
import com.example.jp.repositories.Topics.SourceCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SourceCodeService {

    @Autowired
    private SourceCodeRepository sourceCodeRepository;

    public List<SourceCode> getAllSourceCodes() {
        return sourceCodeRepository.findAll();
    }
    public List<SourceCode> getCodeByTopics(Long topicsId) {
        return sourceCodeRepository.findByTopicId(topicsId);
    }
    public Optional<SourceCode> getSourceCodeById(Long id) {
        return sourceCodeRepository.findById(id);
    }

    public SourceCode saveSourceCode(SourceCode sourceCode) {
        return sourceCodeRepository.save(sourceCode);
    }

    public void deleteSourceCode(Long id) {
        sourceCodeRepository.deleteById(id);
    }
}
