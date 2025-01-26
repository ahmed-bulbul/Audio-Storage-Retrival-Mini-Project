package com.audio.storage.service.impl;


import com.audio.storage.constant.CommonConstant;
import com.audio.storage.entity.AudioFile;
import com.audio.storage.entity.Phrase;
import com.audio.storage.entity.User;
import com.audio.storage.exception.DataNotFoundException;
import com.audio.storage.exception.UnsupportedFormatException;
import com.audio.storage.repository.AudioFileRepository;
import com.audio.storage.repository.PhraseRepository;
import com.audio.storage.repository.UserRepository;
import com.audio.storage.service.AudioFileService;
import com.audio.storage.utils.AudioConversionUtility;
import com.audio.storage.utils.ValidatorUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.*;

import java.io.File;
import java.io.IOException;
import java.util.Optional;


@Service
public class AudioFileServiceImpl implements AudioFileService {
    private final UserRepository userRepository;
    private final PhraseRepository phraseRepository;
    private final AudioFileRepository audioFileRepository;

    @Value("${audio.storage.path}")
    private String storagePath;


    public AudioFileServiceImpl(UserRepository userRepository, PhraseRepository phraseRepository,
                                AudioFileRepository audioFileRepository) {
        this.userRepository = userRepository;
        this.phraseRepository = phraseRepository;
        this.audioFileRepository = audioFileRepository;
    }

    public void save(MultipartFile file, Long userId, Long phraseId) throws IOException, EncoderException, UnsupportedFormatException {
        // Validate file format
        String fileType = AudioConversionUtility.getFileType(file);
        if (ValidatorUtility.isInvalidFormat(fileType)) {
            throw new UnsupportedFormatException(CommonConstant.UNSUPPORTED_AUDIO_FORMAT);
        }

        // Fetch User and Phrase in a single call for validation
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));
        Phrase phrase = phraseRepository.findById(phraseId)
                .orElseThrow(() -> new DataNotFoundException("Phrase not found"));

        // Validate file
        Optional.of(file)
                .filter(f -> !f.isEmpty())
                .orElseThrow(() -> new DataNotFoundException("File is empty or null"));

        // Generate file path
        String filePath = AudioConversionUtility.getFilePath(file, storagePath);

        // Retrieve or create an AudioFile entity
        AudioFile audioFile = audioFileRepository.findByUserIdAndPhraseId(userId, phraseId)
                .map(existingAudio -> {
                    existingAudio.setFilePath(filePath);
                    return existingAudio;
                })
                .orElseGet(() -> buildAudioFile(user, phrase, filePath));

        // Save the AudioFile
        audioFileRepository.save(audioFile);
    }


    private static AudioFile buildAudioFile(User user, Phrase phrase, String filePath) {
        AudioFile audioFile = new AudioFile();
        audioFile.setUser(user);
        audioFile.setPhrase(phrase);
        audioFile.setFilePath(filePath);
        return audioFile;
    }



    public Resource get(Long userId, Long phraseId, String format) throws EncoderException, IOException, UnsupportedFormatException {
        // Retrieve the AudioFile or throw an exception if not found
        AudioFile audioFile = audioFileRepository.findByUserIdAndPhraseId(userId, phraseId)
                .orElseThrow(() -> new DataNotFoundException("Audio file not found"));

        // Validate the format
        Optional.ofNullable(format)
                .filter(f -> !ValidatorUtility.isInvalidFormat(f))
                .orElseThrow(() -> new UnsupportedFormatException(CommonConstant.UNSUPPORTED_AUDIO_FORMAT));

        // Convert the file to the requested format
        File wavFile = new File(audioFile.getFilePath());
        File convertedFile = AudioConversionUtility.convertToFormat(wavFile, format);

        // Return the converted file as a resource
        return new FileSystemResource(convertedFile);
    }


}
