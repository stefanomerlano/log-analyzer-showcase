package it.showcase.loganalyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App {
    public static void main(String[] args) {
        System.out.println("Concurrent Log Analyzer starting...");

        // A thread-safe list to hold all the successfully parsed log entries from all
        // files.
        List<LogEntry> allLogEntries = new ArrayList<>();

        // Use a fixed-size thread pool. A good starting point is the number of
        // available processors.
        int coreCount = Runtime.getRuntime().availableProcessors();
        ExecutorService executorService = Executors.newFixedThreadPool(coreCount);

        // This list will hold the results of our concurrent tasks.
        List<Future<List<LogEntry>>> futures = new ArrayList<>();

        // Determine the resource path
        String resourcesDir;
        if (args.length > 0) {
            resourcesDir = args[0];
        } else {
            resourcesDir = "build/resources/main";
        }

        try {
            Path resourcesPath = Paths.get(resourcesDir);
            if (!Files.exists(resourcesPath) || !Files.isDirectory(resourcesPath)) {
                System.out.println("Resource directory not found: " + resourcesDir);
                return;
            }
            try (Stream<Path> paths = Files.walk(resourcesPath)) {
                List<Path> logFiles = paths
                        .filter(Files::isRegularFile)
                        .filter(path -> path.toString().endsWith(".log"))
                        .collect(Collectors.toList());

                if (logFiles.isEmpty()) {
                    System.out.println("No log files found in resources folder: " + resourcesDir);
                    return;
                }

                System.out.println("Found " + logFiles.size() + " log files to process in " + resourcesDir + "...");

                // For each file, create and submit a parsing task to the executor.
                for (Path logFile : logFiles) {
                    Callable<List<LogEntry>> task = () -> parseFile(logFile);
                    futures.add(executorService.submit(task));
                }
            }
        } catch (Exception e) {
            System.err.println("Error finding log files: " + e.getMessage());
            e.printStackTrace();
        }

        // Wait for all tasks to complete and collect their results.
        for (Future<List<LogEntry>> future : futures) {
            try {
                // future.get() blocks until the task is complete and returns its result.
                allLogEntries.addAll(future.get());
            } catch (InterruptedException | ExecutionException e) {
                System.err.println("Error processing a log file task: " + e.getMessage());
            }
        }

        // It's crucial to shut down the executor service when you're done with it.
        executorService.shutdown();

        // --- DATA ANALYSIS WITH STREAMS ---
        if (!allLogEntries.isEmpty()) {
            analyzeLogs(allLogEntries);
        }

        System.out.println("\nLog Analyzer finished.");
    }

    /**
     * Parses a single log file. This method is designed to be run in a separate
     * thread.
     * 
     * @param path The path to the log file.
     * @return A list of parsed LogEntry objects.
     */
    private static List<LogEntry> parseFile(Path path) {
        System.out.println("Processing file: " + path.getFileName() + " on thread " + Thread.currentThread().getName());
        List<LogEntry> entries = new ArrayList<>();
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(line -> {
                LogEntry entry = LogParser.parseLine(line);
                if (entry != null) {
                    entries.add(entry);
                }
            });
        } catch (IOException e) {
            System.err.println("Error reading file " + path.getFileName() + ": " + e.getMessage());
        }
        return entries;
    }

    /**
     * Performs and prints data analysis on the collected log entries using Java
     * Streams.
     * 
     * @param entries The list of all parsed log entries.
     */
    private static void analyzeLogs(List<LogEntry> entries) {
        System.out.println("\n--- Starting Data Analysis ---");
        System.out.println("Total entries collected: " + entries.size());

        // 1. Count log entries by level.
        // This is a classic "group by" operation.
        Map<LogLevel, Long> countByLevel = entries.stream()
                .collect(Collectors.groupingBy(
                        LogEntry::level, // Group by the 'level' field of the LogEntry
                        Collectors.counting() // Count the elements in each group
                ));

        System.out.println("\n1. Log count by level:");
        countByLevel.forEach((level, count) -> System.out.println("  - " + level + ": " + count));

        // 2. Filter to find all ERROR messages.
        List<LogEntry> errorLogs = entries.stream()
                .filter(entry -> entry.level() == LogLevel.ERROR) // Keep only entries where level is ERROR
                .collect(Collectors.toList()); // Collect the results into a new list

        System.out.println("\n2. Found " + errorLogs.size() + " ERROR entries:");
        errorLogs.forEach(entry -> System.out.println("  - " + entry));

        System.out.println("\n--- Analysis Complete ---");
    }
}
