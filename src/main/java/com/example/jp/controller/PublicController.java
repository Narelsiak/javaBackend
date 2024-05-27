package com.example.jp.controller;

import com.example.jp.model.Category;
import com.example.jp.model.Presentations;
import com.example.jp.services.CategoryService;
import com.example.jp.services.PresentationService;
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
    @GetMapping("/categories")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @GetMapping("/presentations")
    public List<Presentations> getAllPresentations(@RequestParam(required = false) Long category) {
        if(category == null) {
            return presentationService.getAllPresentations();
        }else{
            return presentationService.getPresentationsByCategory(category);
        }
    }
}
