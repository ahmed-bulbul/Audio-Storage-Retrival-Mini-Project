package com.audio.storage.controller;


import com.audio.storage.common.MessageResponse;
import com.audio.storage.exception.UnsupportedFormatException;
import com.audio.storage.service.AudioFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Upload audio file")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Audio file uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping(value="/user/{user_id}/phrase/{phrase_id}", consumes = "multipart/form-data")
    public ResponseEntity<MessageResponse> uploadAudio(
            @PathVariable(value = "user_id") Long userId,
            @PathVariable(value = "phrase_id") Long phraseId,
            @Parameter(description = "Audio file to upload", required = true, content = @Content(
                    mediaType = "multipart/form-data",
                    schema = @Schema(type = "string", format = "binary")
            ))
            @RequestParam("audio_file") MultipartFile file) throws EncoderException, IOException, UnsupportedFormatException {

         audioFileService.save(file,userId, phraseId );
        return ResponseEntity.ok(new MessageResponse("Audio file uploaded successfully"));
    }

    @Operation(summary = "Get audio file")
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
