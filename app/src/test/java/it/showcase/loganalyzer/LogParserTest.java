package it.showcase.loganalyzer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

/**
 * Unit tests for the LogParser class.
 * This class ensures that the parser correctly handles various line formats.
 */
class LogParserTest {

    @Test
    void testParseValidLine() {
        String line = "[2025-08-12T10:00:00] [INFO] - Application starting up...";
        LogEntry entry = LogParser.parseLine(line);

        assertNotNull(entry, "Parsing a valid line should not return null.");
        assertEquals(LocalDateTime.of(2025, 8, 12, 10, 0, 0), entry.timestamp());
        assertEquals(LogLevel.INFO, entry.level());
        assertEquals("Application starting up...", entry.message());
    }

    @Test
    void testParseErrorLine() {
        String line = "[2025-08-12T10:00:08] [ERROR] - Authentication failed for user 'guest'.";
        LogEntry entry = LogParser.parseLine(line);

        assertNotNull(entry);
        assertEquals(LogLevel.ERROR, entry.level());
        assertEquals("Authentication failed for user 'guest'.", entry.message());
    }

    @Test
    void testParseInvalidLineFormat() {
        String line = "This is an invalid line and should be ignored.";
        LogEntry entry = LogParser.parseLine(line);
        assertNull(entry, "Parsing a completely invalid line should return null.");
    }

    @Test
    void testParseLineWithInvalidLogLevel() {
        String line = "[2025-08-12T10:00:21] [FATAL] - This is an unknown log level.";
        LogEntry entry = LogParser.parseLine(line);
        assertNull(entry, "Parsing a line with an unknown log level should return null.");
    }

    @Test
    void testParseLineWithInvalidTimestamp() {
        String line = "[invalid-timestamp] [INFO] - This timestamp is malformed.";
        LogEntry entry = LogParser.parseLine(line);
        assertNull(entry, "Parsing a line with a malformed timestamp should return null.");
    }

    @Test
    void testParseEmptyOrNullLine() {
        assertNull(LogParser.parseLine(null), "Parsing a null line should return null.");
        assertNull(LogParser.parseLine(""), "Parsing an empty line should return null.");
        assertNull(LogParser.parseLine("   "), "Parsing a blank line should return null.");
    }
}
