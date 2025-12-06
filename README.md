# Build Challenge

**Author:** Sawan Chakraborty  
**Date:** December 2025

## Project Overview

This project contains two Java programming assignments demonstrating concurrent programming and functional programming skills:

- **Assignment 1**: Producer-Consumer pattern with thread synchronization
- **Assignment 2**: CSV data analysis using Streams API and functional programming

Both assignments include comprehensive unit tests and demonstrate clean, modular code design.

## Requirements

- **Java 21** (JDK 21 or higher)
- Maven 3.x
- IntelliJ IDEA (recommended) or any Java IDE

### Dependencies
- JUnit 5 (for testing)
- OpenCSV (for CSV parsing)

## Project Structure

```
build-challenge/
├── pom.xml                          # Maven configuration
├── README.md                        # This file
├── data/
│   └── sales.csv                    # Sample sales data (25 records)
├── src/
│   ├── main/java/io/github/sawanc/
│   │   ├── Main.java                # Demo application entry point
│   │   ├── assignment1/
│   │   │   ├── Producer.java        # Producer thread implementation
│   │   │   ├── Consumer.java        # Consumer thread implementation
│   │   │   ├── SourceContainer.java # Thread-safe source container
│   │   │   └── DestinationContainer.java # Thread-safe destination
│   │   └── assignment2/
│   │       ├── SalesRecord.java     # Sales data POJO
│   │       ├── CSVReader.java       # CSV file reader utility
│   │       └── SalesAnalysisService.java # Data analysis service
│   └── test/java/io/github/sawanc/
│       ├── assignment1/
│       │   └── ProducerConsumerTest.java  # 11 unit tests
│       └── assignment2/
│           └── SalesAnalysisServiceTest.java # 19 unit tests
```

**Total:** 30 unit tests with 100% pass rate

## Quick Start

```bash
# 1. Clone or download the repository
cd build-challenge-sawan

# 2. Build the project
mvn clean install

# 3. Run Assignment 1 Demo
mvn exec:java -Dexec.mainClass="io.github.sawanc.Main" -Dexec.args="assignment1"

# 4. Run Assignment 2 Demo
mvn exec:java -Dexec.mainClass="io.github.sawanc.Main" -Dexec.args="assignment2"
```

## How to Test

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=ProducerConsumerTest
mvn test -Dtest=SalesAnalysisServiceTest
```

## Assignments Overview

### Assignment 1: Producer-Consumer Pattern

A classic implementation of the producer-consumer pattern demonstrating thread synchronization and concurrent programming.

#### Features
- **Thread-Safe Containers**: SourceContainer and DestinationContainer with synchronized methods
- **Blocking Queue**: Uses `ArrayBlockingQueue` for thread-safe data transfer
- **Multiple Producers/Consumers**: Supports concurrent producers and consumers
- **Poison Pill Pattern**: Graceful shutdown using sentinel values
- **Thread Synchronization**: Demonstrates wait/notify mechanisms through BlockingQueue

#### Components

**SourceContainer**: Thread-safe container that holds items to be produced
- Synchronized methods for reading items
- Supports resetting for reusability

**DestinationContainer**: Thread-safe container that stores consumed items
- Synchronized methods for adding and retrieving items
- Returns immutable views of data

**Producer**: Runnable thread that reads from SourceContainer and places items into shared queue
- Configurable delay for demonstration
- Tracks items produced
- Optional poison pill insertion

**Consumer**: Runnable thread that reads from shared queue and stores in DestinationContainer
- Configurable timeout and delay
- Tracks items consumed
- Handles poison pill for graceful shutdown

#### Running Assignment 1

**Run the Demo**:

```bash
mvn exec:java -Dexec.mainClass="io.github.sawanc.Main" -Dexec.args="assignment1"
```

**Run Unit Tests**:

```bash
# Run all Assignment 1 tests
mvn test -Dtest=ProducerConsumerTest

# Run specific test
mvn test -Dtest=ProducerConsumerTest#testSingleProducerSingleConsumer
```

#### Sample Output

```
Assignment 1: Producer-Consumer Pattern Demo
=============================================

Configuration:
  - Source items: 20
  - Queue capacity: 10
  - Producers: 2
  - Consumers: 2

Starting producer and consumer threads...

[Producer-34] Started producing...
[Consumer-36] Started consuming...
[Producer-35] Started producing...
[Consumer-37] Started consuming...
[Consumer-37] Consumed: 10 (Total consumed: 1)
[Producer-35] Produced: 10 (Queue size: 1)
[Producer-34] Produced: 20 (Queue size: 1)
[Consumer-36] Consumed: 20 (Total consumed: 1)
...
[Producer-34] Finished. Total items produced: 12
[Producer-35] Finished. Total items produced: 8
[Consumer-36] Received poison pill, stopping consumption
[Consumer-37] Received poison pill, stopping consumption
[Consumer-36] Finished. Total items consumed: 12
[Consumer-37] Finished. Total items consumed: 8

============================================================
RESULTS
============================================================
Producer 1 produced: 12 items
Producer 2 produced: 8 items
Consumer 1 consumed: 12 items
Consumer 2 consumed: 8 items

Total produced: 20
Total consumed: 20
Queue empty: true

Items in destination (first 10): [10, 20, 30, 40, 50, 60, 70, 80, 90, 100]

✓ SUCCESS: All items transferred correctly!
============================================================
```

#### Implementation Highlights

**Thread Synchronization Techniques**:
- `synchronized` keyword for method-level locking
- `BlockingQueue` for producer-consumer coordination
- `put()` and `take()`/`poll()` operations for blocking behavior
- Poison pill pattern (sentinel value) for graceful shutdown

**Concurrency Features**:
- Multiple producers can write to the queue simultaneously
- Multiple consumers can read from the queue simultaneously
- Thread-safe containers prevent race conditions
- Proper handling of `InterruptedException`

**Testing Coverage**:
- Single producer, single consumer
- Multiple producers, single consumer
- Single producer, multiple consumers
- Blocking queue capacity handling
- Empty source container edge case
- Thread safety verification
- Stop functionality
- 11 comprehensive unit tests with 100% pass rate

**Code Quality**:
- Comprehensive Javadoc documentation
- Detailed logging for debugging
- Configurable delays for demonstration
- Clean separation of concerns
- Exception handling throughout

### Assignment 2: Sales Data Analysis

A functional programming-based sales data analysis application using Java 21 Streams API.

#### Features
- **CSV Data Reading**: Parse sales data from CSV files with error handling
- **Functional Programming**: Extensive use of Streams, lambdas, and method references
- **Multiple Analyses**:
  - Total sales calculation
  - Sales grouped by category
  - Sales grouped by region
  - Average sales per product
  - High-value transaction filtering

#### Dataset Description
The `data/sales.csv` file contains 25 sample sales records with the following structure:
- **Product**: Product name (e.g., Laptop, Desk, Notebook)
- **Category**: Product category (Electronics, Furniture, Stationery)
- **Region**: Sales region (North, South, East, West)
- **Amount**: Sale amount in USD

**Dataset Assumptions**:
- All amounts are in USD
- Same products can appear multiple times (repeat purchases)
- Regional distribution is balanced across North, South, East, and West
- Categories represent common business segments

#### Running Assignment 2

**Run the Demo:**

```bash
mvn exec:java -Dexec.mainClass="io.github.sawanc.Main" -Dexec.args="assignment2"
```

**Run Unit Tests:**

```bash
# Run all Assignment 2 tests
mvn test -Dtest=SalesAnalysisServiceTest

# Run specific test
mvn test -Dtest=SalesAnalysisServiceTest#testGetTotalSales
```

#### Sample Output

Running the demo with `mvn exec:java -Dexec.mainClass="io.github.sawanc.Main" -Dexec.args="assignment2"`:

```
Assignment 2: CSV Data Analysis Demo
=====================================

Reading sales data from: data/sales.csv
Successfully loaded 25 sales records.

============================================================
SALES ANALYSIS SUMMARY
============================================================

1. TOTAL SALES: $8196.35

2. SALES BY CATEGORY:
   Stationery     : $85.45
   Furniture      : $2864.46
   Electronics    : $5246.44

3. SALES BY REGION:
   South          : $1925.96
   North          : $2199.45
   West           : $1158.98
   East           : $2911.96

4. AVERAGE SALES PER PRODUCT:
   Laptop         : $1183.50
   Calculator     : $29.99
   Mouse          : $25.99
   Keyboard       : $77.50
   Pen Set        : $12.25
   Monitor        : $612.50
   Stapler        : $15.99
   Notebook       : $5.99
   Headphones     : $144.99
   Bookshelf      : $212.25
   Desk           : $475.00
   Cabinet        : $387.50
   Folder         : $8.99
   Lamp           : $89.99
   Chair          : $312.50

5. HIGH-VALUE TRANSACTIONS (>= $500.00):
   SalesRecord{product='Laptop', category='Electronics', region='North', amount=1200.50}
   SalesRecord{product='Monitor', category='Electronics', region='East', amount=599.99}
   SalesRecord{product='Laptop', category='Electronics', region='South', amount=1150.00}
   SalesRecord{product='Monitor', category='Electronics', region='West', amount=625.00}
   SalesRecord{product='Laptop', category='Electronics', region='East', amount=1199.99}

============================================================
```

#### Implementation Highlights

**Functional Programming Techniques**:
- Method references: `SalesRecord::getAmount`, `SalesRecord::getCategory`
- Lambda expressions: `record -> record.getAmount() >= threshold`
- Stream operations: `stream()`, `filter()`, `mapToDouble()`, `collect()`
- Collectors: `groupingBy()`, `summingDouble()`, `averagingDouble()`
- Immutable collections: `Map.copyOf()`

**Code Quality**:
- Comprehensive Javadoc documentation
- 19 unit tests covering all methods and edge cases
- Error handling for file I/O and CSV parsing
- Clean separation of concerns (POJO, Reader, Service)

---

## Testing Results

```bash
mvn test
```

**Output:**
```
Tests run: 30, Failures: 0, Errors: 0, Skipped: 0
BUILD SUCCESS
```

All tests pass successfully, demonstrating:
- Correct thread synchronization (Assignment 1)
- Accurate data analysis algorithms (Assignment 2)
- Proper edge case handling
- Thread-safe concurrent operations

---

## Author

**Sawan Chakraborty**
