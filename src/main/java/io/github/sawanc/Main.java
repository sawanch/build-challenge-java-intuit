package io.github.sawanc;

import io.github.sawanc.assignment1.*;
import io.github.sawanc.assignment2.CSVReader;
import io.github.sawanc.assignment2.SalesAnalysisService;
import io.github.sawanc.assignment2.SalesRecord;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Main entry point for demonstrating Assignment 1 and Assignment 2.
 *
 * @author sawan chakraborty
 */
public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("assignment1")) {
            runAssignment1Demo();
        } else if (args.length > 0 && args[0].equals("assignment2")) {
            runAssignment2Demo();
        } else {
            System.out.println("Build Challenge - Java Implementation");
            System.out.println("=====================================\n");
            System.out.println("Available demos:");
            System.out.println("  mvn exec:java -Dexec.mainClass=\"io.github.sawanc.Main\" -Dexec.args=\"assignment1\"");
            System.out.println("  mvn exec:java -Dexec.mainClass=\"io.github.sawanc.Main\" -Dexec.args=\"assignment2\"");
            System.out.println("\nOr run tests:");
            System.out.println("  mvn test");
        }
    }

    /**
     * Demonstrates Assignment 1: Producer-Consumer Pattern with Thread Synchronization.
     * Creates multiple producers and consumers that transfer data through a shared blocking queue.
     */
    private static void runAssignment1Demo() {
        System.out.println("Assignment 1: Producer-Consumer Pattern Demo");
        System.out.println("=============================================\n");

        try {
            // Create source data
            List<Integer> sourceData = new ArrayList<>();
            for (int i = 1; i <= 20; i++) {
                sourceData.add(i * 10);
            }

            // Create containers and shared queue
            SourceContainer source = new SourceContainer(sourceData);
            DestinationContainer destination = new DestinationContainer();
            BlockingQueue<Integer> sharedQueue = new ArrayBlockingQueue<>(10);

            System.out.println("Configuration:");
            System.out.println("  - Source items: " + source.size());
            System.out.println("  - Queue capacity: 10");
            System.out.println("  - Producers: 2");
            System.out.println("  - Consumers: 2\n");

            // Create producers and consumers
            Producer producer1 = new Producer(source, sharedQueue, false, 100);
            Producer producer2 = new Producer(source, sharedQueue, false, 150);
            Consumer consumer1 = new Consumer(destination, sharedQueue, 5000, 120);
            Consumer consumer2 = new Consumer(destination, sharedQueue, 5000, 180);

            // Start threads
            Thread p1 = new Thread(producer1, "Producer-1");
            Thread p2 = new Thread(producer2, "Producer-2");
            Thread c1 = new Thread(consumer1, "Consumer-1");
            Thread c2 = new Thread(consumer2, "Consumer-2");

            System.out.println("Starting producer and consumer threads...\n");
            p1.start();
            p2.start();
            c1.start();
            c2.start();

            // Wait for producers to finish
            p1.join();
            p2.join();

            // Send poison pills to stop consumers
            sharedQueue.put(Producer.POISON_PILL);
            sharedQueue.put(Producer.POISON_PILL);

            // Wait for consumers to finish
            c1.join();
            c2.join();

            // Print results
            System.out.println("\n" + "=".repeat(60));
            System.out.println("RESULTS");
            System.out.println("=".repeat(60));
            System.out.println("Producer 1 produced: " + producer1.getItemsProduced() + " items");
            System.out.println("Producer 2 produced: " + producer2.getItemsProduced() + " items");
            System.out.println("Consumer 1 consumed: " + consumer1.getItemsConsumed() + " items");
            System.out.println("Consumer 2 consumed: " + consumer2.getItemsConsumed() + " items");
            System.out.println("\nTotal produced: " + (producer1.getItemsProduced() + producer2.getItemsProduced()));
            System.out.println("Total consumed: " + destination.size());
            System.out.println("Queue empty: " + sharedQueue.isEmpty());
            
            System.out.println("\nItems in destination (first 10): " + 
                    destination.getAllItems().stream().limit(10).toList());
            
            if (destination.size() == source.size()) {
                System.out.println("\n✓ SUCCESS: All items transferred correctly!");
            } else {
                System.out.println("\n✗ ERROR: Item count mismatch!");
            }
            System.out.println("=".repeat(60));

        } catch (Exception e) {
            System.err.println("Error during demo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Demonstrates Assignment 2: CSV Data Analysis with Functional Programming.
     * Reads sales data from CSV and performs various analytical operations.
     */
    private static void runAssignment2Demo() {
        System.out.println("Assignment 2: CSV Data Analysis Demo");
        System.out.println("=====================================\n");

        try {
            // Read sales data from CSV file
            String csvPath = "data/sales.csv";
            System.out.println("Reading sales data from: " + csvPath);
            List<SalesRecord> records = CSVReader.readSalesData(csvPath);
            System.out.println("Successfully loaded " + records.size() + " sales records.\n");

            // Create analysis service
            SalesAnalysisService service = new SalesAnalysisService();

            // Print comprehensive summary
            service.printSummary(records);

        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error during analysis: " + e.getMessage());
            e.printStackTrace();
        }
    }
}