package com.audio.storage.service;

import com.audio.storage.exception.UnsupportedFormatException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.EncoderException;

import java.io.IOException;

public interface AudioFileService {



    public Resource get(Long userId, Long phraseId, String format) throws EncoderException, IOException, UnsupportedFormatException;

    public void save(MultipartFile file, Long userId, Long phraseId) throws IOException, EncoderException, UnsupportedFormatException;
}
