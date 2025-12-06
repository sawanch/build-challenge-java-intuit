package io.github.sawanc.assignment1;

import java.util.concurrent.BlockingQueue;

/**
 * Producer thread that reads items from a SourceContainer and places them into a shared BlockingQueue.
 * 
 * @author sawan chakraborty
 */
public class Producer implements Runnable {
    // Sentinel value used as poison pill to signal end of production
    public static final Integer POISON_PILL = Integer.MIN_VALUE;
    
    private final SourceContainer source;
    private final BlockingQueue<Integer> sharedQueue;
    private final boolean addPoisonPill;
    private final long delayMs;
    private volatile boolean stopped = false;
    private volatile int itemsProduced = 0;

    /**
     * Constructs a Producer with default settings (adds poison pill, no delay).
     * 
     * @param source the source container
     * @param sharedQueue the shared blocking queue
     */
    public Producer(SourceContainer source, BlockingQueue<Integer> sharedQueue) {
        this(source, sharedQueue, true, 0);
    }

    /**
     * Constructs a Producer with configurable poison pill and delay.
     * 
     * @param source the source container
     * @param sharedQueue the shared blocking queue
     * @param addPoisonPill whether to add poison pill when done
     * @param delayMs delay between producing items in milliseconds
     */
    public Producer(SourceContainer source, BlockingQueue<Integer> sharedQueue, 
                    boolean addPoisonPill, long delayMs) {
        if (source == null || sharedQueue == null) {
            throw new IllegalArgumentException("Source and sharedQueue cannot be null");
        }
        this.source = source;
        this.sharedQueue = sharedQueue;
        this.addPoisonPill = addPoisonPill;
        this.delayMs = delayMs;
    }

    /**
     * Runs the producer thread.
     */
    @Override
    public void run() {
        try {
            System.out.println("[Producer-" + Thread.currentThread().threadId() + "] Started producing...");
            
            // Produce all items from the source
            while (!stopped && source.hasNext()) {
                Integer item = source.getNext();
                
                // Place item into the shared queue (blocks if queue is full)
                sharedQueue.put(item);
                itemsProduced++;
                
                System.out.println("[Producer-" + Thread.currentThread().threadId() + "] Produced: " + item 
                        + " (Queue size: " + sharedQueue.size() + ")");
                
                // Optional delay for testing/demonstration
                if (delayMs > 0) {
                    Thread.sleep(delayMs);
                }
            }
            
            // Add poison pill to signal consumers that production is complete
            if (addPoisonPill && !stopped) {
                sharedQueue.put(POISON_PILL);
                System.out.println("[Producer-" + Thread.currentThread().threadId() 
                        + "] Added poison pill (production complete)");
            }
            
            System.out.println("[Producer-" + Thread.currentThread().threadId() 
                    + "] Finished. Total items produced: " + itemsProduced);
            
        } catch (InterruptedException e) {
            System.err.println("[Producer-" + Thread.currentThread().threadId() + "] Interrupted");
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            System.err.println("[Producer-" + Thread.currentThread().threadId() + "] Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void stop() {
        this.stopped = true;
    }

    public int getItemsProduced() {
        return itemsProduced;
    }

    public boolean isStopped() {
        return stopped;
    }
}
