package com.audio.storage.service;

import com.audio.storage.dto.PhraseRequestDto;

public interface PhraseService {
    String create(PhraseRequestDto requestDto);
}
