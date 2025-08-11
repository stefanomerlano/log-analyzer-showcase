package it.showcase.loganalyzer;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A utility class for parsing log file lines.
 * It provides a static method to convert a raw string line into a structured LogEntry object.
 */
public class LogParser {

    // Pre-compile the regular expression for efficiency. This is a best practice.
    // The regex is designed to capture three groups from a line like:
    // "[2025-08-11T23:30:00] [INFO] - The message"
    // Group 1: (.*) -> The timestamp inside the first brackets.
    // Group 2: (.*) -> The log level inside the second brackets.
    // Group 3: (.*) -> The rest of the line, which is the message.
    private static final Pattern LOG_PATTERN = Pattern.compile("^\\[(.*)\\] \\[(.*)\\] - (.*)$");

    /**
     * Parses a single log line string into a LogEntry object.
     *
     * @param line The raw string line from the log file.
     * @return A {@link LogEntry} object if parsing is successful, or {@code null} if the
     * line format is invalid or data cannot be parsed.
     */
    public static LogEntry parseLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }

        Matcher matcher = LOG_PATTERN.matcher(line);

        if (matcher.find()) {
            try {
                // Extract the captured groups from the matcher.
                String timestampStr = matcher.group(1);
                String levelStr = matcher.group(2);
                String message = matcher.group(3);

                // Parse the extracted strings into their corresponding types.
                LocalDateTime timestamp = LocalDateTime.parse(timestampStr);
                LogLevel level = LogLevel.valueOf(levelStr.toUpperCase());

                // Create and return the structured LogEntry record.
                return new LogEntry(timestamp, level, message);

            } catch (DateTimeParseException e) {
                System.err.println("Could not parse timestamp: " + e.getMessage());
                return null;
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid log level found: " + matcher.group(2));
                return null;
            }
        }

        // If the line does not match the expected pattern.
        System.err.println("Line does not match expected log format: \"" + line + "\"");
        return null;
    }
}
