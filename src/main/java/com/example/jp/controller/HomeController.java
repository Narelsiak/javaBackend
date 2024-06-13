package com.example.jp.controller;

import com.example.jp.model.Presentations.Presentations;
import com.example.jp.model.Test.Test;
import com.example.jp.services.Presentations.CategoryService;
import com.example.jp.services.Presentations.PresentationService;
import com.example.jp.services.Test.TestService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomeController {

    private final PresentationService presentationService;
    private final CategoryService categoryService;
    private final TestService testService;

    public HomeController(PresentationService presentationService, CategoryService categoryService, TestService testService) {
        this.presentationService = presentationService;
        this.categoryService = categoryService;
        this.testService = testService;
    }

    @GetMapping("/")
    public String index(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
        return "mainPage";
    }

    @GetMapping("admin/presentation")
    public String presentations(){
        return "presentations";
    }
    @GetMapping("admin/category")
    public String categories(){
        return "categories";
    }
    @GetMapping("admin/topic")
    public String topic(){
        return "topics";
    }

    @GetMapping("admin/sourcecode")
    public String sourcecode(){
        return "sourcecodes";
    }

    @GetMapping("admin/link")
    public String link(){
        return "link";
    }

    @GetMapping("admin/test")
    public String test(){
        return "test";
    }

    @GetMapping("admin/test/add")
    public String testadd(){
        return "test_add";
    }

    @GetMapping("admin/test/show/{id}")
    public String testshow(@PathVariable Long id, Model model){
        Test test = testService.getTestByIdWithQuestions(id);
        model.addAttribute("test", test);
        return "showTest";
    }
    @GetMapping("admin/test/show/result/{id}")
    public String testshowResult(@PathVariable Long id, Model model){
        Test test = testService.getTestByIdWithQuestions(id);
        model.addAttribute("test", test);
        return "showTestResult";
    }

    @GetMapping("/header")
    public String header(){
        return "header";
    }
}