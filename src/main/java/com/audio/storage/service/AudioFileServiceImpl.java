package com.audio.storage.service;


import com.audio.storage.constant.CommonConstant;
import com.audio.storage.entity.AudioFile;
import com.audio.storage.entity.Phrase;
import com.audio.storage.entity.User;
import com.audio.storage.exception.DataNotFoundException;
import com.audio.storage.exception.UnsupportedFormatException;
import com.audio.storage.repository.AudioFileRepository;
import com.audio.storage.repository.PhraseRepository;
import com.audio.storage.repository.UserRepository;
import com.audio.storage.utils.AudioConversionUtility;
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
@Slf4j
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

        if(AudioConversionUtility.isInvalidFormat(AudioConversionUtility.getFileType(file))) {
            throw new UnsupportedFormatException(CommonConstant.UNSUPPORTED_AUDIO_FORMAT);
        }

        if(userRepository.findById(userId).isEmpty()) {
            throw new DataNotFoundException("User not found");
        }

        if(phraseRepository.findById(phraseId).isEmpty()) {
            throw new DataNotFoundException("Phrase not found");
        }

        if(file.isEmpty()) {
            throw new DataNotFoundException("File is empty");
        }

        User user = userRepository.findById(userId).orElse(null);
        Phrase phrase = phraseRepository.findById(phraseId).orElse(null);

        String filePath = AudioConversionUtility.getFilePath(file,storagePath);

        Optional<AudioFile> existingAudioFile = audioFileRepository.findByUserIdAndPhraseId(userId, phraseId);
        AudioFile audioFile;
        if (existingAudioFile.isPresent()) {
            audioFile = existingAudioFile.get();
            audioFile.setFilePath(filePath);
        }else{
            audioFile = buildAudioFile(user, phrase, filePath);
        }

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
        AudioFile audioFile = audioFileRepository.findByUserIdAndPhraseId(userId, phraseId)
                .orElseThrow(() -> new DataNotFoundException("Audio file not found"));

        if (AudioConversionUtility.isInvalidFormat(format)) {
            throw new UnsupportedFormatException(CommonConstant.UNSUPPORTED_AUDIO_FORMAT);
        }

        File wavFile = new File(audioFile.getFilePath());
        File convertedFile = AudioConversionUtility.convertToFormat(wavFile, format);

        return new FileSystemResource(convertedFile);
    }

}
