package io.github.sawanc.assignment1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Consumer thread that reads items from a shared BlockingQueue and stores them in a DestinationContainer.
 * 
 * @author sawan chakraborty
 */
public class Consumer implements Runnable {
    private final DestinationContainer destination;
    private final BlockingQueue<Integer> sharedQueue;
    private final long timeoutMs;
    private final long delayMs;
    private volatile boolean stopped = false;
    private volatile int itemsConsumed = 0;

    /**
     * Constructs a Consumer with default timeout and no delay.
     * 
     * @param destination the destination container
     * @param sharedQueue the shared blocking queue
     */
    public Consumer(DestinationContainer destination, BlockingQueue<Integer> sharedQueue) {
        this(destination, sharedQueue, 5000, 0);
    }

    /**
     * Constructs a Consumer with configurable timeout and delay.
     * 
     * @param destination the destination container
     * @param sharedQueue the shared blocking queue
     * @param timeoutMs timeout in milliseconds (0 = infinite wait)
     * @param delayMs delay between consuming items in milliseconds
     */
    public Consumer(DestinationContainer destination, BlockingQueue<Integer> sharedQueue, 
                    long timeoutMs, long delayMs) {
        if (destination == null || sharedQueue == null) {
            throw new IllegalArgumentException("Destination and sharedQueue cannot be null");
        }
        this.destination = destination;
        this.sharedQueue = sharedQueue;
        this.timeoutMs = timeoutMs;
        this.delayMs = delayMs;
    }

    /**
     * Runs the consumer thread.
     */
    @Override
    public void run() {
        try {
            System.out.println("[Consumer-" + Thread.currentThread().threadId() + "] Started consuming...");
            
            while (!stopped) {
                Integer item;
                
                // Poll from the shared queue with timeout
                if (timeoutMs > 0) {
                    item = sharedQueue.poll(timeoutMs, TimeUnit.MILLISECONDS);
                    
                    // Timeout occurred, check if we should continue
                    if (item == null && !stopped) {
                        System.out.println("[Consumer-" + Thread.currentThread().threadId() 
                                + "] Timeout waiting for items, checking again...");
                        continue;
                    }
                } else {
                    // Block indefinitely until an item is available
                    item = sharedQueue.take();
                }
                
                // Poison pill received (POISON_PILL sentinel value) - stop consuming
                if (item != null && item.equals(Producer.POISON_PILL)) {
                    System.out.println("[Consumer-" + Thread.currentThread().threadId() 
                            + "] Received poison pill, stopping consumption");
                    break;
                }
                
                // Null item (from timeout) - continue waiting
                if (item == null) {
                    continue;
                }
                
                // Store item in destination container
                destination.add(item);
                itemsConsumed++;
                
                System.out.println("[Consumer-" + Thread.currentThread().threadId() + "] Consumed: " + item 
                        + " (Total consumed: " + itemsConsumed + ")");
                
                // Optional delay for testing/demonstration
                if (delayMs > 0) {
                    Thread.sleep(delayMs);
                }
            }
            
            System.out.println("[Consumer-" + Thread.currentThread().threadId() 
                    + "] Finished. Total items consumed: " + itemsConsumed);
            
        } catch (InterruptedException e) {
            System.err.println("[Consumer-" + Thread.currentThread().threadId() + "] Interrupted");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[Consumer-" + Thread.currentThread().threadId() + "] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stop() {
        this.stopped = true;
    }

    public int getItemsConsumed() {
        return itemsConsumed;
    }

    public boolean isStopped() {
        return stopped;
    }
}
