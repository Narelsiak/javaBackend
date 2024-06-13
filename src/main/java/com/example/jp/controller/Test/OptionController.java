package com.example.jp.controller.Test;

import com.example.jp.model.Test.Option;
import com.example.jp.repositories.Test.OptionRepository;
import com.example.jp.services.Test.OptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin/options")
public class OptionController {
    @Autowired
    private OptionService optionService;
    @Autowired
    private OptionRepository optionRepository;

    @GetMapping("/{optionId}")
    public Option getOptionById(@PathVariable Long optionId) {
        return optionService.getOptionById(optionId);
    }

    @PostMapping("/create")
    public Option createOption(@RequestBody Option option) {
        return optionService.createOption(option);
    }
    @DeleteMapping("/delete/{id}")
    public Option deleteOption(@PathVariable Long id) {
        Option option = optionService.getOptionById(id);
        optionRepository.delete(option);
        return option;
    }

    // Dodaj inne metody kontrolera w zależności od potrzeb, np. do edycji, usuwania odpowiedzi, etc.
}
