package com.example.jp.services;

import com.example.jp.model.Presentations;
import com.example.jp.repositories.PresentationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PresentationService {

    private final PresentationsRepository presentationRepository;

    public PresentationService(PresentationsRepository presentationRepository) {
        this.presentationRepository = presentationRepository;
    }

    public List<Presentations> getAllPresentations() {
        return presentationRepository.findAll();
    }

    public Optional<Presentations> getPresentationById(Long id) {
        return presentationRepository.findById(id);
    }

    public Presentations createPresentation(Presentations presentation) {
        return presentationRepository.save(presentation);
    }

    public Presentations updatePresentation(Long id, Presentations updatedPresentation) {
        Optional<Presentations> existingPresentationOptional = presentationRepository.findById(id);
        if (existingPresentationOptional.isPresent()) {
            Presentations existingPresentation = existingPresentationOptional.get();
            existingPresentation.setName(updatedPresentation.getName());
            existingPresentation.setCategory(updatedPresentation.getCategory());
            return presentationRepository.save(existingPresentation);
        }
        return null;
    }

    public void deletePresentation(Long id) {
        presentationRepository.deleteById(id);
    }
}
