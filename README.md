# Build Challenge

**Author:** Sawan Chakraborty  
**Date:** December 2025

## Project Overview

This project consists of 2 programming assignments solved in Java — one covering multithreading concepts and another focused on functional programming with the Streams API.

- **Assignment 1**: Producer-Consumer pattern with thread synchronization
- **Assignment 2**: CSV data analysis using Streams API and functional programming

Both assignments include unit tests and follow clean, modular code design practices.

## Requirements

- **Java 8+** (Project tested on JDK 21)
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
│   └── sales.csv                    # Sales data (100 orders)
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
│           ├── SalesAnalysisServiceTest.java # 19 unit tests
│           └── CSVReaderTest.java           # 11 unit tests
```

**Total:** 41 unit tests, all passing successfully

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
mvn test -Dtest=CSVReaderTest
```

## Assignments Overview

### Assignment 1: Producer-Consumer Pattern

An implementation of the producer–consumer pattern demonstrating thread synchronization and concurrent programming.

#### Features
- **Thread-Safe Containers**: SourceContainer and DestinationContainer with synchronized methods
- **Blocking Queue**: Uses `ArrayBlockingQueue` for thread-safe data transfer
- **Multiple Producers/Consumers**: Supports concurrent producers and consumers
- **Poison Pill Pattern**: Graceful shutdown using sentinel values
- **Thread Synchronization**: Uses BlockingQueue for safe coordination between threads

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

![Assignment 1 Console Output](screenshots/assignment1-output.png?raw=true)

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
[Producer-34] Produced: 10
[Consumer-36] Consumed: 10
[Producer-35] Produced: 20
[Consumer-37] Consumed: 20
[Producer-34] Produced: 30
[Consumer-36] Consumed: 30
[Producer-35] Produced: 40
[Consumer-37] Consumed: 40
[Producer-34] Produced: 50
[Consumer-36] Consumed: 50
[Producer-35] Produced: 60
[Consumer-37] Consumed: 60
[Producer-34] Produced: 70
[Consumer-36] Consumed: 70
[Producer-35] Produced: 80
[Consumer-37] Consumed: 80
[Producer-34] Produced: 90
[Consumer-36] Consumed: 90
[Producer-35] Produced: 100
[Consumer-37] Consumed: 100
[Producer-34] Produced: 110
[Consumer-36] Consumed: 110
[Producer-35] Produced: 120
[Consumer-37] Consumed: 120
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

A functional programming-based sales data analysis application using Java Streams API.

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

The `data/sales.csv` file contains **100 sales order records** with the following structure:

**CSV Columns** (7 fields):
- **order_id**: Unique order identifier (ORD-001 to ORD-100)
- **product**: Product name (e.g., Laptop, Desk, Phone, Sofa)
- **category**: Product category (Electronics, Furniture, Stationery)
- **region**: Sales region as continent names (North America, Europe, Asia, Africa, South America, Australia)
- **amount**: Sale amount in USD (range: $4.99 - $1,299.99)
- **quantity**: Number of units sold per order (range: 1-20 units)
- **order_date**: Order date in YYYY-MM-DD format (January - April 2025)

**Dataset Choices and Assumptions**:
- **100 orders** provide sufficient data volume to demonstrate Stream operations effectively
- **Continent-based regions** offer realistic geographical distribution for global sales analysis
- **3 product categories** (Electronics, Furniture, Stationery) represent common business segments
- All monetary amounts are in USD
- Same products can appear in multiple orders (representing repeat purchases)
- Dates are distributed across 4 months to show temporal patterns
- Quantity field enables unit-based analysis in addition to revenue analysis
- Dataset constructed to demonstrate functional programming competencies: grouping, aggregation, filtering, and lambda expressions

OpenCSV is used as the CSV parsing library, which meets the requirement to use an appropriate API for handling CSV data.

#### Running Assignment 2

**Run the Demo:**

```bash
mvn exec:java -Dexec.mainClass="io.github.sawanc.Main" -Dexec.args="assignment2"
```

**Run Unit Tests:**

```bash
# Run all Assignment 2 tests
mvn test -Dtest=SalesAnalysisServiceTest,CSVReaderTest

# Run specific test
mvn test -Dtest=SalesAnalysisServiceTest#testGetTotalSales
mvn test -Dtest=CSVReaderTest#testReadValidCSV
```

#### Sample Output

![Assignment 2 Console Output](screenshots/assignment2-output.png)

Running the demo with `mvn exec:java -Dexec.mainClass="io.github.sawanc.Main" -Dexec.args="assignment2"`:

```
Assignment 2: CSV Data Analysis Demo
=====================================

Reading sales data from: data/sales.csv
Successfully loaded 100 sales records.

============================================================
SALES ANALYSIS SUMMARY
============================================================

1. TOTAL SALES: $30602.62

2. SALES BY CATEGORY:
   Furniture      : $14739.22
   Electronics    : $15359.20
   Stationery     : $504.20

3. SALES BY REGION:
   South America  : $5996.84
   Europe         : $4536.34
   Africa         : $6138.84
   Australia      : $1875.37
   North America  : $6502.37
   Asia           : $5552.86

4. AVERAGE SALES PER PRODUCT:
   Notebook       : $6.99
   Desk           : $482.49
   Nightstand     : $159.99
   Sofa           : $949.99
   Envelope       : $11.66
   Lamp           : $89.99
   Scanner        : $189.99
   Wardrobe       : $809.99
   Cabinet        : $397.50
   Dresser        : $623.32
   Calculator     : $32.66
   Keyboard       : $77.50
   Printer        : $276.66
   Tablet         : $549.99
   Monitor        : $613.74
   Paper          : $26.66
   Headphones     : $162.49
   Binder         : $21.66
   Phone          : $849.99
   Marker Set     : $17.99
   Bookshelf      : $226.12
   Folder         : $9.99
   Tape           : $5.99
   Webcam         : $103.32
   Chair          : $323.74
   Laptop         : $1190.09
   Table          : $373.32
   Mouse          : $27.99
   Pen Set        : $13.37
   Stapler        : $16.99

5. HIGH-VALUE TRANSACTIONS (>= $500.00):
   SalesRecord{orderId='ORD-001', product='Laptop', category='Electronics', region='North America', amount=1200.50, quantity=1, date='2025-01-05'}
   SalesRecord{orderId='ORD-004', product='Monitor', category='Electronics', region='Africa', amount=599.99, quantity=1, date='2025-01-08'}
   SalesRecord{orderId='ORD-016', product='Laptop', category='Electronics', region='Africa', amount=1150.00, quantity=1, date='2025-01-20'}
   SalesRecord{orderId='ORD-019', product='Monitor', category='Electronics', region='North America', amount=625.00, quantity=1, date='2025-01-23'}
   SalesRecord{orderId='ORD-023', product='Laptop', category='Electronics', region='South America', amount=1199.99, quantity=1, date='2025-01-27'}
   SalesRecord{orderId='ORD-026', product='Tablet', category='Electronics', region='Europe', amount=549.99, quantity=1, date='2025-02-01'}
   SalesRecord{orderId='ORD-027', product='Phone', category='Electronics', region='Asia', amount=899.99, quantity=1, date='2025-02-02'}
   SalesRecord{orderId='ORD-031', product='Sofa', category='Furniture', region='North America', amount=899.99, quantity=1, date='2025-02-06'}
   SalesRecord{orderId='ORD-033', product='Dresser', category='Furniture', region='Asia', amount=599.99, quantity=1, date='2025-02-08'}
   SalesRecord{orderId='ORD-034', product='Wardrobe', category='Furniture', region='Africa', amount=799.99, quantity=1, date='2025-02-09'}
   SalesRecord{orderId='ORD-041', product='Laptop', category='Electronics', region='South America', amount=1299.99, quantity=1, date='2025-02-16'}
   SalesRecord{orderId='ORD-044', product='Monitor', category='Electronics', region='Europe', amount=649.99, quantity=1, date='2025-02-19'}
   SalesRecord{orderId='ORD-056', product='Tablet', category='Electronics', region='Europe', amount=549.99, quantity=1, date='2025-03-03'}
   SalesRecord{orderId='ORD-057', product='Phone', category='Electronics', region='Asia', amount=799.99, quantity=1, date='2025-03-04'}
   SalesRecord{orderId='ORD-061', product='Sofa', category='Furniture', region='North America', amount=999.99, quantity=1, date='2025-03-08'}
   SalesRecord{orderId='ORD-063', product='Dresser', category='Furniture', region='Asia', amount=649.99, quantity=1, date='2025-03-10'}
   SalesRecord{orderId='ORD-064', product='Wardrobe', category='Furniture', region='Africa', amount=849.99, quantity=1, date='2025-03-11'}
   SalesRecord{orderId='ORD-071', product='Laptop', category='Electronics', region='South America', amount=1099.99, quantity=1, date='2025-03-18'}
   SalesRecord{orderId='ORD-074', product='Monitor', category='Electronics', region='Europe', amount=579.99, quantity=1, date='2025-03-21'}
   SalesRecord{orderId='ORD-086', product='Tablet', category='Electronics', region='Europe', amount=599.99, quantity=1, date='2025-04-02'}
   SalesRecord{orderId='ORD-087', product='Phone', category='Electronics', region='Asia', amount=849.99, quantity=1, date='2025-04-03'}
   SalesRecord{orderId='ORD-091', product='Sofa', category='Furniture', region='North America', amount=949.99, quantity=1, date='2025-04-07'}
   SalesRecord{orderId='ORD-093', product='Dresser', category='Furniture', region='Asia', amount=619.99, quantity=1, date='2025-04-09'}
   SalesRecord{orderId='ORD-094', product='Wardrobe', category='Furniture', region='Africa', amount=779.99, quantity=1, date='2025-04-10'}

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
- 30 unit tests covering all methods and edge cases (19 for SalesAnalysisService, 11 for CSVReader)
- Error handling for file I/O and CSV parsing
- Clean separation of concerns (POJO, Reader, Service)

---

## Testing Results

```bash
mvn test
```

**Output:**
```
Tests run: 41, Failures: 0, Errors: 0, Skipped: 0
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
