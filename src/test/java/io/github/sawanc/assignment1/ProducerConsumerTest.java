package io.github.sawanc.assignment1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Timeout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the Producer-Consumer pattern implementation.
 *
 * @author sawan chakraborty
 */
public class ProducerConsumerTest {

    private SourceContainer source;
    private DestinationContainer destination;
    private BlockingQueue<Integer> sharedQueue;

    @BeforeEach
    public void setUp() {
        // Create test data
        List<Integer> testData = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        source = new SourceContainer(testData);
        destination = new DestinationContainer();
        sharedQueue = new ArrayBlockingQueue<>(5); // Limited capacity to test blocking
    }

    /**
     * Tests basic producer-consumer operation with a single producer and single consumer.
     * Verifies that all items are transferred correctly.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testSingleProducerSingleConsumer() throws InterruptedException {
        // Create producer and consumer
        Producer producer = new Producer(source, sharedQueue);
        Consumer consumer = new Consumer(destination, sharedQueue);

        // Start threads
        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

        // Wait for completion
        producerThread.join();
        consumerThread.join();

        // Verify results
        assertEquals(source.size(), destination.size(), 
                "All items should be transferred from source to destination");
        assertEquals(source.getAllItems(), destination.getAllItems(), 
                "Items should be transferred in order");
        assertEquals(10, producer.getItemsProduced(), "Producer should produce 10 items");
        assertEquals(10, consumer.getItemsConsumed(), "Consumer should consume 10 items");
        assertTrue(sharedQueue.isEmpty(), "Queue should be empty after completion");
    }

    /**
     * Tests the producer-consumer pattern with multiple producers.
     * Verifies thread synchronization when multiple threads produce items.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void testMultipleProducersSingleConsumer() throws InterruptedException {
        // Create multiple sources
        List<Integer> data1 = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> data2 = Arrays.asList(6, 7, 8, 9, 10);
        List<Integer> data3 = Arrays.asList(11, 12, 13, 14, 15);

        SourceContainer source1 = new SourceContainer(data1);
        SourceContainer source2 = new SourceContainer(data2);
        SourceContainer source3 = new SourceContainer(data3);

        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        // Create producers (none add poison pill initially)
        Producer producer1 = new Producer(source1, queue, false, 0);
        Producer producer2 = new Producer(source2, queue, false, 0);
        Producer producer3 = new Producer(source3, queue, false, 0);

        Consumer consumer = new Consumer(destination, queue, 5000, 0);

        // Start all threads
        Thread p1 = new Thread(producer1);
        Thread p2 = new Thread(producer2);
        Thread p3 = new Thread(producer3);
        Thread c1 = new Thread(consumer);

        p1.start();
        p2.start();
        p3.start();
        c1.start();

        // Wait for all producers to finish
        p1.join();
        p2.join();
        p3.join();
        
        // Now add poison pill after ALL producers have finished
        queue.put(Producer.POISON_PILL);
        
        // Wait for consumer to finish
        c1.join();

        // Verify all 15 items were consumed
        assertEquals(15, destination.size(), "All 15 items should be consumed");
        assertEquals(15, consumer.getItemsConsumed(), "Consumer should consume 15 items");
    }

    /**
     * Tests the producer-consumer pattern with multiple consumers.
     * Verifies thread synchronization when multiple threads consume items.
     */
    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    public void testSingleProducerMultipleConsumers() throws InterruptedException {
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 
                                          11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        SourceContainer src = new SourceContainer(data);
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);

        // Create multiple consumers
        DestinationContainer dest1 = new DestinationContainer();
        DestinationContainer dest2 = new DestinationContainer();
        DestinationContainer dest3 = new DestinationContainer();

        Producer producer = new Producer(src, queue, false, 0); // No poison pill yet

        Consumer consumer1 = new Consumer(dest1, queue, 2000, 0);
        Consumer consumer2 = new Consumer(dest2, queue, 2000, 0);
        Consumer consumer3 = new Consumer(dest3, queue, 2000, 0);

        // Start producer first
        Thread producerThread = new Thread(producer);
        producerThread.start();

        // Start consumers
        Thread c1 = new Thread(consumer1);
        Thread c2 = new Thread(consumer2);
        Thread c3 = new Thread(consumer3);

        c1.start();
        c2.start();
        c3.start();

        // Wait for producer to finish
        producerThread.join();

        // Add poison pills for each consumer
        queue.put(Producer.POISON_PILL);
        queue.put(Producer.POISON_PILL);
        queue.put(Producer.POISON_PILL);

        // Wait for consumers
        c1.join();
        c2.join();
        c3.join();

        // Verify total items consumed
        int totalConsumed = dest1.size() + dest2.size() + dest3.size();
        assertEquals(20, totalConsumed, "All 20 items should be consumed by the three consumers");
        assertEquals(20, producer.getItemsProduced(), "Producer should produce 20 items");
    }

    /**
     * Tests that the BlockingQueue properly blocks when full.
     * Verifies the blocking behavior of the producer when queue capacity is reached.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testBlockingQueueFullBehavior() throws InterruptedException {
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        SourceContainer src = new SourceContainer(data);
        BlockingQueue<Integer> smallQueue = new ArrayBlockingQueue<>(3); // Small capacity

        Producer producer = new Producer(src, smallQueue, false, 0);
        
        // Start producer without consumer - queue will fill up and block
        Thread producerThread = new Thread(producer);
        producerThread.start();

        // Wait a bit for queue to fill
        Thread.sleep(100);

        // Queue should be full
        assertTrue(smallQueue.size() >= 3, "Queue should be at or near capacity");

        // Now start consumer to drain the queue
        DestinationContainer dest = new DestinationContainer();
        Consumer consumer = new Consumer(dest, smallQueue);
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        // Add poison pill
        producerThread.join();
        smallQueue.put(Producer.POISON_PILL);

        // Wait for completion
        consumerThread.join();

        // Verify all items transferred
        assertEquals(10, dest.size(), "All items should eventually be transferred");
    }

    /**
     * Tests SourceContainer thread safety and functionality.
     */
    @Test
    public void testSourceContainer() {
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        SourceContainer src = new SourceContainer(data);

        assertEquals(5, src.size());
        assertTrue(src.hasNext());

        // Read all items
        List<Integer> retrieved = new ArrayList<>();
        while (src.hasNext()) {
            retrieved.add(src.getNext());
        }

        assertEquals(data, retrieved, "Retrieved items should match original data");
        assertFalse(src.hasNext(), "Should have no more items");

        // Test reset
        src.reset();
        assertTrue(src.hasNext(), "After reset, should have items again");
        assertEquals(Integer.valueOf(1), src.getNext());
    }

    /**
     * Tests DestinationContainer thread safety and functionality.
     */
    @Test
    public void testDestinationContainer() {
        DestinationContainer dest = new DestinationContainer();

        assertTrue(dest.isEmpty());
        assertEquals(0, dest.size());

        dest.add(1);
        dest.add(2);
        dest.add(3);

        assertEquals(3, dest.size());
        assertFalse(dest.isEmpty());

        List<Integer> items = dest.getAllItems();
        assertEquals(Arrays.asList(1, 2, 3), items);

        // Test that returned list is unmodifiable
        assertThrows(UnsupportedOperationException.class, () -> {
            items.add(4);
        });

        dest.clear();
        assertTrue(dest.isEmpty());
    }

    /**
     * Tests edge case with empty source container.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testEmptySourceContainer() throws InterruptedException {
        SourceContainer emptySource = new SourceContainer(new ArrayList<>());
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        DestinationContainer dest = new DestinationContainer();

        Producer producer = new Producer(emptySource, queue);
        Consumer consumer = new Consumer(dest, queue);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        assertEquals(0, dest.size(), "Destination should be empty");
        assertEquals(0, producer.getItemsProduced(), "Producer should produce 0 items");
    }

    /**
     * Tests producer with single item.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testSingleItem() throws InterruptedException {
        List<Integer> singleItem = Arrays.asList(42);
        SourceContainer src = new SourceContainer(singleItem);
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(5);
        DestinationContainer dest = new DestinationContainer();

        Producer producer = new Producer(src, queue);
        Consumer consumer = new Consumer(dest, queue);

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        assertEquals(1, dest.size(), "Should have one item");
        assertEquals(Integer.valueOf(42), dest.getAllItems().get(0), "Item should be 42");
    }

    /**
     * Tests exception handling for null arguments.
     */
    @Test
    public void testNullArgumentsThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Producer(null, sharedQueue);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Producer(source, null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Consumer(null, sharedQueue);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            new Consumer(destination, null);
        });
    }

    /**
     * Tests the stop functionality of producer and consumer.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testStopFunctionality() throws InterruptedException {
        List<Integer> largeData = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            largeData.add(i);
        }

        SourceContainer src = new SourceContainer(largeData);
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(10);
        DestinationContainer dest = new DestinationContainer();

        Producer producer = new Producer(src, queue, false, 10); // Slow producer
        Consumer consumer = new Consumer(dest, queue, 5000, 10); // Slow consumer

        Thread producerThread = new Thread(producer);
        Thread consumerThread = new Thread(consumer);

        producerThread.start();
        consumerThread.start();

        // Let them run for a bit
        Thread.sleep(200);

        // Stop both
        producer.stop();
        consumer.stop();

        assertTrue(producer.isStopped(), "Producer should be stopped");
        assertTrue(consumer.isStopped(), "Consumer should be stopped");

        // Wait for threads to finish
        producerThread.join(2000);
        consumerThread.join(2000);

        // Should not have processed all items
        assertTrue(dest.size() < 100, "Should have stopped before processing all items");
    }

    /**
     * Tests concurrent modification safety of containers.
     */
    @Test
    @Timeout(value = 5, unit = TimeUnit.SECONDS)
    public void testThreadSafety() throws InterruptedException {
        List<Integer> data = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            data.add(i);
        }

        SourceContainer src = new SourceContainer(data);
        BlockingQueue<Integer> queue = new ArrayBlockingQueue<>(20);
        DestinationContainer dest = new DestinationContainer();

        // Create multiple producers and consumers
        Producer p1 = new Producer(src, queue, false, 0);
        Consumer c1 = new Consumer(dest, queue, 3000, 0);
        Consumer c2 = new Consumer(dest, queue, 3000, 0);

        Thread pt1 = new Thread(p1);
        Thread ct1 = new Thread(c1);
        Thread ct2 = new Thread(c2);

        pt1.start();
        ct1.start();
        ct2.start();

        pt1.join();
        
        // Add poison pills
        queue.put(Producer.POISON_PILL);
        queue.put(Producer.POISON_PILL);

        ct1.join();
        ct2.join();

        // Verify all items consumed (distributed among consumers)
        assertEquals(50, dest.size(), "All items should be consumed");
    }
}
