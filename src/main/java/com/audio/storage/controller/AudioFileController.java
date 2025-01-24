package com.audio.storage.controller;


import com.audio.storage.common.MessageResponse;
import com.audio.storage.exception.UnsupportedFormatException;
import com.audio.storage.service.AudioFileService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.IOException;


@RestController
@RequestMapping("/audio")
public class AudioFileController {

    private final AudioFileService audioFileService;

    public AudioFileController(AudioFileService audioFileService) {
        this.audioFileService = audioFileService;
    }

    @PostMapping("/user/{user_id}/phrase/{phrase_id}")
    public ResponseEntity<MessageResponse> uploadAudio(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "phrase_id") Long phraseId,
            @RequestParam("audio_file") MultipartFile file) throws EncoderException, IOException, UnsupportedFormatException {

         audioFileService.save(file,userId, phraseId );
        return ResponseEntity.ok(new MessageResponse("Audio file uploaded successfully"));
    }

    @GetMapping("/user/{user_id}/phrase/{phrase_id}/{audio_format}")
    public ResponseEntity<Resource> get(@PathVariable(value = "user_id") Long userId,
                                                  @PathVariable(value = "phrase_id") Long phraseId,
                                                  @PathVariable(value = "audio_format") String audioFormat) throws IOException, EncoderException, UnsupportedFormatException {

        Resource file = audioFileService.get(userId, phraseId, audioFormat);

        String contentType = "audio/" + audioFormat.toLowerCase();

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))  // Set content type for audio
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"audio." + audioFormat + "\"")
                .body(file);
    }







}
