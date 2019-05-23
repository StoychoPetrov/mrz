package com.example.mrz.logger;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

public class LoggerFactory {

    private static LoggerLevel level = LoggerLevel.INFO;

    private static final Map<String, AndroidLogger> loggerCache;

    static {
        loggerCache = new HashMap<>();
    }

    public static LoggerLevel getLevel() {
        return level;
    }

    public static void setLevel(LoggerLevel loggerLevel) {
        if (loggerLevel != null) {
            level = loggerLevel;
        }
    }

    /**
     * Sets the logger level based on a String
     * @param loggerLevelString string is the name of the logger level (enum name in String)
     */
    public static void setLevelFromString(String loggerLevelString) {
        LoggerLevel level = LoggerLevel.getLoggerLevel(loggerLevelString);

        if (level != LoggerLevel.NONE) {
            setLevel(level);
        }
    }

    /**
     * Sets the logger level based on a String resource, taken from Android XML files
     * @param context The Android Context object. Can be either an Application object or an Activity
     * @param stringResourceId the R.string integer value to lookup the String on Android resources
     */
    public static void setLevelFromStringResource(Context context, int stringResourceId) {
        String loggerLevelString = context.getString(stringResourceId);
        setLevelFromString(loggerLevelString);
    }

    public static Logger getLogger(Class<?> clazz) {
        String tag = null;
        if (clazz != null) {
            tag = clazz.getSimpleName();
        }
        return getLogger(tag);
    }

    public static Logger getLogger(String tag) {
        AndroidLogger logger = null;

        synchronized (loggerCache) {
            logger = loggerCache.get(tag);
            if (logger == null) {
                logger = new AndroidLogger(tag);
                loggerCache.put(tag, logger);
            }
        }

        return logger;
    }
}
