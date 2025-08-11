package it.showcase.loganalyzer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class LogTest {
    // This class is intended for unit tests related to LogEntry and LogLevel

    @Test
    public void testLogEntryCreation() {
        LogEntry entry = new LogEntry(LocalDateTime.now(), LogLevel.INFO, "Test message");
        assertNotNull(entry);
        assertEquals(LogLevel.INFO, entry.level());
        assertEquals("Test message", entry.message());
    }
}
