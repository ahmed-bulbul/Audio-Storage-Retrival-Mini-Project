package com.audio.storage.constant;

public class CommonConstant {
    public static final String USER_HOME = System.getProperty("user.home")+"\\";
    public static final String TEMP_FILE_DIR = USER_HOME;

    public static final String UNDERSCORE = "_";

    public static final String COLON = " : ";

    public static final String FILE_SEPARATOR = "/";

    public static final String UNSUPPORTED_AUDIO_FORMAT = "Unsupported audio format."+
            "Supported formats are "+COLON+"mp3, ogg";
}
