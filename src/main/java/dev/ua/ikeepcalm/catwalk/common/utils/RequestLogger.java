package dev.ua.ikeepcalm.catwalk.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Dedicated access log for inbound HTTP requests. Kept separate from the console/server
 * log (and rotated independently) so request auditing - who called what, with what
 * authorization - doesn't get lost in the regular server log.
 */
public class RequestLogger {

    private static final Logger LOG = Logger.getLogger("CatWalkRequests");
    private static FileHandler fileHandler;
    private static volatile boolean enabled = false;

    private RequestLogger() {
    }

    public static synchronized void initialize(File dataFolder, boolean loggingEnabled) {
        shutdown();

        if (!loggingEnabled) {
            return;
        }

        try {
            File logsDir = new File(dataFolder, "logs");
            if (!logsDir.exists() && !logsDir.mkdirs()) {
                CatWalkLogger.warn("Could not create logs directory at %s", logsDir.getAbsolutePath());
                return;
            }

            // Rotates at 10MB, keeps 5 files, appends across restarts.
            fileHandler = new FileHandler(new File(logsDir, "requests.log").getAbsolutePath(), 10 * 1024 * 1024, 5, true);
            fileHandler.setFormatter(new SimpleFormatter() {
                @Override
                public String format(LogRecord record) {
                    return record.getMessage() + System.lineSeparator();
                }
            });

            LOG.setUseParentHandlers(false);
            LOG.setLevel(Level.ALL);
            LOG.addHandler(fileHandler);
            enabled = true;
        } catch (IOException e) {
            CatWalkLogger.warn("Failed to initialize request log file: %s", e.getMessage());
        }
    }

    public static void log(String message) {
        if (!enabled) {
            return;
        }
        LOG.info(message);
    }

    public static synchronized void shutdown() {
        if (fileHandler != null) {
            LOG.removeHandler(fileHandler);
            fileHandler.close();
            fileHandler = null;
        }
        enabled = false;
    }
}
