package com.example.jp.controller.Topics;

import com.example.jp.model.Presentations.Presentations;
import com.example.jp.model.Topics.Link;
import com.example.jp.model.Topics.Topics;
import com.example.jp.services.Topics.LinkService;
import com.example.jp.services.Topics.TopicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("admin/links")
public class LinkController {

    @Autowired
    private LinkService linkService;

    @Autowired
    private TopicsService topicService;

    @GetMapping
    public List<Link> getAllLinks(@RequestParam(required = false) Long topic) {
        if (topic == null) {
            return linkService.getAllLinks();
        } else {
            return linkService.getLinksByTopic(topic);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Link> getLinkById(@PathVariable("id") Long id) {
        Optional<Link> link = linkService.getLinkById(id);
        return link.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<String> createLink(@RequestParam("title") String title,
                                             @RequestParam("description") String description,
                                             @RequestParam("url") String url,
                                             @RequestParam("topic") Long topicId) {
        Optional<Topics> topicOptional = topicService.getTopicsById(topicId);
        if (topicOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("Nieprawidłowy temat.");
        }
        Topics topic = topicOptional.get();
        Link newLink = new Link();
        newLink.setTitle(title);
        newLink.setDescription(description);
        newLink.setLink(url);
        newLink.setTopic(topic);
        linkService.saveLink(newLink);
        return ResponseEntity.ok("Link created successfully");
    }

    @PutMapping("/{id}")
    public Link updateLink(@PathVariable Long id, @RequestBody Link link) {
        return linkService.updateLink(id, link);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLink(@PathVariable("id") Long id) {
        linkService.deleteLink(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
