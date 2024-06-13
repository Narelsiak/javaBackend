package com.example.jp.services.Test;

import com.example.jp.model.Test.Option;
import com.example.jp.repositories.Test.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OptionService {
    @Autowired
    private OptionRepository optionRepository;

    public Option getOptionById(Long optionId) {
        return optionRepository.findById(optionId).orElse(null);
    }

    public Option createOption(Option option) {
        return optionRepository.save(option);
    }

    // Dodaj inne metody serwisu w zależności od potrzeb, np. do edycji, usuwania odpowiedzi, etc.
}
