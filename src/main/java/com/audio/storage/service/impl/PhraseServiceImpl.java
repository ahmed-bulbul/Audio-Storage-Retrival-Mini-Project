package com.audio.storage.service.impl;

import com.audio.storage.dto.PhraseRequestDto;
import com.audio.storage.entity.Phrase;
import com.audio.storage.repository.PhraseRepository;
import com.audio.storage.service.PhraseService;
import org.springframework.stereotype.Service;

@Service
public class PhraseServiceImpl implements PhraseService {

    private final PhraseRepository phraseRepository;

    public PhraseServiceImpl(PhraseRepository phraseRepository) {
        this.phraseRepository = phraseRepository;
    }

    @Override
    public String create(PhraseRequestDto requestDto) {
        Phrase phrase = new Phrase(requestDto.getText());
        phraseRepository.save(phrase);
        return "Phrase created successfully";
    }
}
