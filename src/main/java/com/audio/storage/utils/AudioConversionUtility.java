package com.audio.storage.utils;


import com.audio.storage.exception.UnsupportedFormatException;
import org.springframework.web.multipart.MultipartFile;
import ws.schild.jave.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;

import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Utility class for audio file conversion and handling.
 */
public class AudioConversionUtility {

    /**
     * Converts an audio file to WAV format, saves it to the specified storage path, and returns the file path.
     *
     * @param file        the input audio file
     * @param storagePath the directory where the WAV file will be stored
     * @return the path of the saved WAV file
     * @throws IOException      if an I/O error occurs
     * @throws EncoderException if the encoding process fails
     */
    public static String getFilePath(MultipartFile file, String storagePath) throws IOException, EncoderException {
        File wavFile = convertToWav(file);
        // Ensure the directory exists
        Files.createDirectories(Paths.get(storagePath));

        // Generate the final file path
        String fileName = generateFileName(file) + ".wav";
        String filePath = storagePath + File.separator + fileName;

        // Move the WAV file to the target location
        File finalFile = new File(filePath);
        if (finalFile.exists()) {
            Files.delete(finalFile.toPath());
        }
        Files.move(wavFile.toPath(), finalFile.toPath());

        return filePath;
    }

    /**
     * Converts an audio file to WAV format.
     *
     * @param file the input audio file
     * @return the converted WAV file
     * @throws IOException      if an I/O error occurs
     * @throws EncoderException if the encoding process fails
     */
    private static File convertToWav(MultipartFile file) throws IOException, EncoderException {
        File tempFile = File.createTempFile("temp_audio", ".mp3");
        file.transferTo(tempFile);

        File wavFile = File.createTempFile("converted_audio", ".wav");

        // Set audio attributes for WAV format
        AudioAttributes audioAttributes = new AudioAttributes();
        audioAttributes.setCodec("pcm_s16le");

        EncodingAttributes encodingAttributes = new EncodingAttributes();
        encodingAttributes.setFormat("wav");
        encodingAttributes.setAudioAttributes(audioAttributes);

        // Perform the conversion
        MultimediaObject multimediaObject = new MultimediaObject(tempFile);
        Encoder encoder = new Encoder();
        encoder.encode(multimediaObject, wavFile, encodingAttributes);

        // Clean up temporary file
        try {
            Files.delete(tempFile.toPath());
        } catch (IOException e) {
            throw new IOException("Failed to delete temporary file: " + tempFile.getAbsolutePath(), e);
        }

        return wavFile;
    }

    /**
     * Generates a file name without the file extension.
     *
     * @param file the input file
     * @return the generated file name
     */
    private static String generateFileName(MultipartFile file) {
        return FilenameUtils.getBaseName(file.getOriginalFilename());
    }

    /**
     * Converts a WAV file to the specified audio format.
     *
     * @param wavFile the input WAV file
     * @param format  the target audio format
     * @return the converted file
     * @throws IOException      if an I/O error occurs
     * @throws EncoderException if the encoding process fails
     */
    public static File convertToFormat(File wavFile, String format) throws IOException, EncoderException, UnsupportedFormatException {
        if (ValidatorUtility.isInvalidFormat(format)) {
            throw new UnsupportedFormatException("Unsupported audio format: " + format);
        }

        String extension = ValidatorUtility.getValidFileExtension(format);
        String codec = ValidatorUtility.getValidCodec(format);

        File convertedFile = File.createTempFile("converted_audio", "." + extension);

        AudioAttributes audioAttributes = new AudioAttributes();
        audioAttributes.setCodec(codec);

        EncodingAttributes encodingAttributes = new EncodingAttributes();
        encodingAttributes.setFormat(format.toLowerCase());
        encodingAttributes.setAudioAttributes(audioAttributes);

        MultimediaObject multimediaObject = new MultimediaObject(wavFile);
        Encoder encoder = new Encoder();
        encoder.encode(multimediaObject, convertedFile, encodingAttributes);

        return convertedFile;
    }


    /**
     * Retrieves the file type (extension) from a MultipartFile.
     *
     * @param file the input file
     * @return the file type
     */
    public static String getFileType(MultipartFile file) {
        return FilenameUtils.getExtension(file.getOriginalFilename());
    }
}

