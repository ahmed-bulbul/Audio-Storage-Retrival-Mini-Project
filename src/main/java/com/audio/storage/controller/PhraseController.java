package com.audio.storage.controller;

import com.audio.storage.common.MessageResponse;
import com.audio.storage.dto.PhraseRequestDto;
import com.audio.storage.service.PhraseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/phrases")
public class PhraseController {

    private final PhraseService phraseService;

    public PhraseController(PhraseService phraseService) {
        this.phraseService = phraseService;
    }

    //create
    @PostMapping
    public ResponseEntity<MessageResponse> create(@RequestBody PhraseRequestDto requestDto){
        return ResponseEntity.ok(new MessageResponse(phraseService.create(requestDto)));
    }
}
