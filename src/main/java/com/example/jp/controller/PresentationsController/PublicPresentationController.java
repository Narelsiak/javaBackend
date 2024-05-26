package com.example.jp.controller.PresentationsController;

import com.example.jp.model.Presentations;
import com.example.jp.services.PresentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/public/presentations")
public class PublicPresentationController {

    private final PresentationService presentationService;

    @Autowired
    public PublicPresentationController(PresentationService presentationService) {
        this.presentationService = presentationService;
    }

    @GetMapping
    public List<Presentations> getAllPresentations() {
        return presentationService.getAllPresentations();
    }

    @GetMapping("/{id}")
    public Optional<Presentations> getPresentationById(@PathVariable Long id) {
        return presentationService.getPresentationById(id);
    }
}
