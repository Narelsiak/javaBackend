package com.example.jp.controller.Presentations;

import com.example.jp.model.Presentations.Category;
import com.example.jp.model.Presentations.Presentations;
import com.example.jp.repositories.Presentations.CategoriesRepository;
import com.example.jp.services.Presentations.CategoryService;
import com.example.jp.services.Presentations.PresentationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("admin/presentations")
public class PresentationController {

    private final PresentationService presentationService;
    private final CategoryService categoryService;


    @Autowired
    public PresentationController(PresentationService presentationService, CategoryService categoryService) {
        this.presentationService = presentationService;
        this.categoryService = categoryService;
    }

    @GetMapping
    public List<Presentations> getAllPresentations(@RequestParam(required = false) Long category) {
        List<Presentations> presentations;
        if(category == null) {
            //presentations = presentationService.getAllPresentations();
            return presentationService.getAllPresentations();
        }else{
            //presentations = presentationService.getPresentationsByCategory(category);
            return presentationService.getPresentationsByCategory(category);
        }
        //return presentations.stream().map(PresentationDTO::new).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public Presentations getPresentationById(@PathVariable Long id) {
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

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            return ResponseEntity.badRequest().body("Niewłaściwy typ pliku. Oczekiwano pliku PDF.");
        }

        try {
            String fileName = UUID.randomUUID() + ".pdf";
            Path uploadDir = Paths.get("data/presentations");
            System.out.println(uploadDir);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            Path filePath = uploadDir.resolve(fileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Category category = categoryService.getCategoryById(categoryId);

            Presentations presentation = new Presentations();
            presentation.setName(name);
            presentation.setDescription(description);
            presentation.setFilePath(filePath.toString());
            presentation.setFileName(fileName);
            presentation.setCategory(category);
            presentationService.createPresentation(presentation);

            return ResponseEntity.ok("Prezentacja została dodana pomyślnie.");
        } catch (IOException e) {
            e.printStackTrace(); // Logowanie szczegółów błędu
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd podczas zapisywania pliku: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public Presentations updatePresentation(@PathVariable Long id,
                                            @RequestParam("name") String name,
                                            @RequestParam("description") String description,
                                            @RequestParam("category_id") Long categoryId) {
        Presentations existingPresentation = presentationService.getPresentationById(id);
        existingPresentation.setName(name);
        existingPresentation.setDescription(description);
        Category category = categoryService.getCategoryById(categoryId);
        if (category != null) {
            existingPresentation.setCategory(category);
        }
        return presentationService.savePresentation(existingPresentation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePresentation(@PathVariable Long id) {
        try {
            Presentations presentation = presentationService.getPresentationById(id);

            Path filePath = Paths.get(presentation.getFilePath());
            Files.deleteIfExists(filePath);

            presentationService.deletePresentation(id);

            return ResponseEntity.ok("Poprawnie usunięto rekord oraz plik.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd podczas usuwania.");
        }
    }
}
