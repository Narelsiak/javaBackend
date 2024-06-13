package com.example.jp.controller.Topics;

import com.example.jp.model.Topics.Link;
import com.example.jp.model.Topics.SourceCode;
import com.example.jp.model.Topics.Topics;
import com.example.jp.services.Topics.SourceCodeService;
import com.example.jp.services.Topics.TopicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admin/sourcecodes")
public class SourceCodeController {

    @Autowired
    private SourceCodeService sourceCodeService;
    @Autowired
    private TopicsService topicService;

    @GetMapping
    public List<SourceCode> getAllSourceCodes(@RequestParam(required = false) Long topic) {
        if(topic == null) {
            return sourceCodeService.getAllSourceCodes();
        }else{
            return sourceCodeService.getCodeByTopics(topic);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SourceCode> getSourceCodeById(@PathVariable("id") Long id) {
        Optional<SourceCode> sourceCode = sourceCodeService.getSourceCodeById(id);
        return sourceCode.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<String> createSourceCode(@RequestParam("title") String title,
                                                   @RequestParam("description") String description,
                                                   @RequestParam("code") String code,
                                                   @RequestParam("topic") Long topicId) {
        Optional<Topics> topicOptional = topicService.getTopicsById(topicId);
        if (topicOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Nieprawidłowy temat.");
        }
        Topics topic = topicOptional.get();
        SourceCode sourceCode = new SourceCode();
        sourceCode.setTitle(title);
        sourceCode.setDescription(description);
        sourceCode.setCode(code);
        sourceCode.setTopic(topic);
        sourceCodeService.saveSourceCode(sourceCode);

        return ResponseEntity.ok("Dodano");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSourceCode(@PathVariable("id") Long id) {
        sourceCodeService.deleteSourceCode(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/{id}")
    public SourceCode updateCode(@PathVariable Long id, @RequestBody SourceCode code) {
        return sourceCodeService.updateCode(id, code);
    }
}
