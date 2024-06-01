package com.example.jp.controller;

import com.example.jp.model.Presentations.Presentations;
import com.example.jp.services.Presentations.CategoryService;
import com.example.jp.services.Presentations.PresentationService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class HomeController {

    private final PresentationService presentationService;
    private final CategoryService categoryService;

    public HomeController(PresentationService presentationService, CategoryService categoryService) {
        this.presentationService = presentationService;
        this.categoryService = categoryService;
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
    @GetMapping("/header")
    public String header(){
        return "header";
    }
}