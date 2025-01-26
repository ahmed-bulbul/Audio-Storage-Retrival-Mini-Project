package com.audio.storage.utils;

import java.util.Arrays;


/**
 * Utility class for validating audio file formats.
 */

public class ValidatorUtility {
    /**
     * Validates if the provided audio format is supported.
     *
     * @param format the audio format to validate
     * @return true if the format is supported, false otherwise
     */
    public static boolean isInvalidFormat(String format) {
        String[] supportedFormats = {"mp3", "ogg", "aac", "flac"};
        return Arrays.stream(supportedFormats).noneMatch(format::equalsIgnoreCase);
    }

    /**
     * Retrieves the file extension for the specified audio format.
     *
     * @param format the audio format
     * @return the file extension
     */
    public static String getValidFileExtension(String format) {
        return switch (format.toLowerCase()) {
            case "mp3" -> "mp3";
            case "ogg" -> "ogg";
            case "aac" -> "aac";
            case "flac" -> "flac";
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };
    }

    /**
     * Retrieves the codec for the specified audio format.
     *
     * @param format the audio format
     * @return the codec
     */
    public static String getValidCodec(String format) {
        return switch (format.toLowerCase()) {
            case "mp3" -> "libmp3lame";
            case "ogg" -> "libvorbis";
            case "flac" -> "flac";
            default -> throw new IllegalArgumentException("Unsupported format: " + format);
        };
    }
}
