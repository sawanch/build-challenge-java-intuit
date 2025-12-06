package io.github.sawanc.assignment1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Thread-safe source container that holds items to be produced.
 * 
 * @author sawan chakraborty
 */
public class SourceContainer {
    private final List<Integer> items;
    private int currentIndex; // Tracks the next item to be produced

    public SourceContainer(List<Integer> items) {
        // Create defensive copy to prevent external modification
        this.items = new ArrayList<>(items);
        this.currentIndex = 0;
    }

    // All methods are synchronized to ensure thread safety
    public synchronized boolean hasNext() {
        return currentIndex < items.size();
    }

    public synchronized Integer getNext() {
        if (!hasNext()) {
            throw new IllegalStateException("No more items in source container");
        }
        return items.get(currentIndex++);
    }

    public synchronized int size() {
        return items.size();
    }

    public synchronized List<Integer> getAllItems() {
        return Collections.unmodifiableList(items);
    }

    public synchronized int getCurrentIndex() {
        return currentIndex;
    }

    // Resets the index to reuse container in tests
    public synchronized void reset() {
        currentIndex = 0;
    }
}
