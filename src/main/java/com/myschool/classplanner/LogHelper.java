package com.myschool.classplanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogHelper.class);

    public static void info(String message, Object... args) {
        LOGGER.info(message, args);
    }
}
