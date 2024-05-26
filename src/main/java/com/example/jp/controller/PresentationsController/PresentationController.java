package com.example.jp.controller.PresentationsController;

import com.example.jp.model.Category;
import com.example.jp.model.Presentations;
import com.example.jp.services.CategoryService;
import com.example.jp.services.PresentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/presentations")
public class PresentationController {

    private final PresentationService presentationService;
    private CategoryService categoryService;


    @Autowired
    public PresentationController(PresentationService presentationService) {
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

    @PostMapping
    public ResponseEntity<String> createPresentation(@RequestParam("name") String name,
                                                     @RequestParam("description") String description,
                                                     @RequestParam("file") MultipartFile file,
                                                     @RequestParam("category_id") Long categoryId) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Plik PDF nie został przesłany.");
        }

        try {
            String fileName = UUID.randomUUID().toString() + ".pdf";
            String filePath = "/src/main/resources/public/presentations/" + fileName;
            System.out.println(filePath);
            Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

            Optional<Category> categoryOptional = categoryService.getCategoryById(categoryId);
            if (categoryOptional.isEmpty()) {
                return ResponseEntity.badRequest().body("Nieprawidłowa kategoria.");
            }
            Category category = categoryOptional.get();

            Presentations presentation = new Presentations();
            presentation.setName(name);
            presentation.setDescription(description);
            presentation.setFilePath(filePath);
            presentation.setCategory(category);
            presentationService.createPresentation(presentation);

            return ResponseEntity.ok("Prezentacja została dodana pomyślnie.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd podczas zapisywania pliku.");
        }
    }

    @PutMapping("/{id}")
    public Presentations updatePresentation(@PathVariable Long id, @RequestBody Presentations presentation) {
        return presentationService.updatePresentation(id, presentation);
    }

    @DeleteMapping("/{id}")
    public void deletePresentation(@PathVariable Long id) {
        presentationService.deletePresentation(id);
    }
}
