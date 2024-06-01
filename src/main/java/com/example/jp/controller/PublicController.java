package com.example.jp.controller;

import com.example.jp.model.Presentations.Category;
import com.example.jp.model.Presentations.Presentations;
import com.example.jp.model.Topics.Link;
import com.example.jp.model.Topics.SourceCode;
import com.example.jp.model.Topics.Topics;
import com.example.jp.services.Presentations.CategoryService;
import com.example.jp.services.Presentations.PresentationService;
import com.example.jp.services.Topics.LinkService;
import com.example.jp.services.Topics.SourceCodeService;
import com.example.jp.services.Topics.TopicsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PresentationService presentationService;
    @Autowired
    private TopicsService topicsService;
    @Autowired
    private SourceCodeService sourceCodeService;
    @Autowired
    private LinkService linkService;


    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategoriesWithPresentations();
    }

    @GetMapping("/presentations")
    public List<Presentations> getAllPresentations(@RequestParam(required = false) Long category) {
        if(category == null) {
            return presentationService.getAllPresentations();
        }else{
            return presentationService.getPresentationsByCategory(category);
        }
    }
    @GetMapping("/topicsCode")
    public List<Topics> getAllTopicsWithCode() {
        return topicsService.getAllTopicsWithCode();
    }

    @GetMapping("/topicsCode/code")
    public List<SourceCode> getAllSourceCodes(@RequestParam(required = false) Long topic) {
        if(topic == null) {
            return sourceCodeService.getAllSourceCodes();
        }else{
            return sourceCodeService.getCodeByTopics(topic);
        }
    }
    @GetMapping("/topicsLink")
    public List<Topics> getAllTopicsWithLink() {
        return topicsService.getAllTopicsWithLink();
    }

    @GetMapping("/topicsLink/link")
    public List<Link> getAllLink(@RequestParam(required = false) Long topic) {
        if(topic == null) {
            return linkService.getAllLinks();
        }else{
            return linkService.getLinksByTopic(topic);
        }
    }

}
