package com.example.jp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String index(){
        return "mainPage";
    }
    @GetMapping("/presentation")
    public String presentations(){
        return "presentations";
    }
    @GetMapping("/category")
    public String categories(){
        return "categories";
    }
    @GetMapping("/header")
    public String header(){
        return "header";
    }
}