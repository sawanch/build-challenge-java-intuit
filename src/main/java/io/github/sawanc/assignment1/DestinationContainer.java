package io.github.sawanc.assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Thread-safe destination container that stores items consumed from the shared queue.
 * 
 * @author sawan chakraborty
 */
public class DestinationContainer {
    private final List<Integer> items;

    public DestinationContainer() {
        this.items = new ArrayList<>();
    }

    // All methods are synchronized to ensure thread safety
    public synchronized void add(Integer item) {
        items.add(item);
    }

    public synchronized int size() {
        return items.size();
    }

    // Returns immutable copy to prevent external modification
    public synchronized List<Integer> getAllItems() {
        return Collections.unmodifiableList(new ArrayList<>(items));
    }

    public synchronized boolean isEmpty() {
        return items.isEmpty();
    }

    public synchronized void clear() {
        items.clear();
    }

    @Override
    public synchronized String toString() {
        return "DestinationContainer{items=" + items + ", size=" + items.size() + "}";
    }
}
