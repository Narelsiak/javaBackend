package com.example.jp.controller.PresentationsController;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class PdfController {

    private static final String UPLOAD_DIR = "data/presentations/";

    @GetMapping("/display-pdf/{fileName:.+}")
    public ResponseEntity<Resource> displayPdf(@PathVariable String fileName) {
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        FileSystemResource file = new FileSystemResource(filePath);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .body(file);
    }
}
