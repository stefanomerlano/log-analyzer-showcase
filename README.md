## Concurrent Log Analyzer in Java

A hands-on project designed to showcase core Java concepts including modern file I/O, multithreading with `ExecutorService`, and data processing with the Streams API. This application reads multiple log files concurrently, parses them, and performs a basic analysis to generate a summary report.

## 🚀 Features

- **Concurrent File Processing:** Utilizes a fixed-size thread pool to process multiple log files in parallel, significantly improving performance.
- **Robust Log Parsing:** Employs regular expressions to parse structured log lines and gracefully handles malformed or invalid entries.
- **Modern Java Syntax:** Built with modern Java features like Records (`LogEntry`), Enums (`LogLevel`), and the `java.nio` package for file operations.
- **Data Analysis with Streams:** Leverages the Java Streams API to perform aggregate operations on the collected log data, such as grouping and filtering.
- **Dynamic File Discovery:** Automatically discovers and processes all `.log` files located in the project's resources folder.

## 🛠️ Tech Stack

- **Language:** Java 17+
- **Build Tool:** Gradle
- **Concurrency:** Java ExecutorService Framework

## ⚙️ How to Run

Clone the repository:

```sh
git clone <your-repository-url>
cd log-analyzer
```

Run the application using the Gradle wrapper:

**On macOS/Linux:**

```sh
./gradlew run
```

**On Windows:**

```sh
gradlew.bat run
```

The application will automatically find the sample `.log` files in the `src/main/resources` directory, process them, and print the analysis to the console.

## 📂 Project Structure

```
.
├── src/main/java/it/showcase/loganalyzer/
│   ├── App.java                 # Main application entry point, orchestrates concurrency and analysis.
│   ├── LogEntry.java            # A Java Record representing a single log entry.
│   ├── LogLevel.java            # An Enum for the different log severity levels.
│   └── LogParser.java           # A utility class to parse string lines into LogEntry objects.
│
└── src/main/resources/
    ├── application.log          # Sample log files for processing.
    ├── service-api.log
    └── service-db.log
```

## 💡 Key Concepts Demonstrated

This project serves as a practical demonstration of the following Java concepts:

### Java Fundamentals

- Strongly-typed data modeling with `record` and `enum`.
- Modern file I/O using `java.nio.file.Path` and `Files.lines()`.
- Robust exception handling with try-catch blocks.

### Java Concurrency

- Management of a thread pool using `ExecutorService` and `Executors`.
- Asynchronous task execution with `Callable`.
- Retrieving results from asynchronous computations using `Future`.
- Proper shutdown of the executor service to release resources.

### Java Streams API

- Processing collections of data in a declarative style.
- Using intermediate operations like `filter()`.
- Using terminal operations like `collect()` with `Collectors.groupingBy()` and `Collectors.counting()` to perform data aggregation.
