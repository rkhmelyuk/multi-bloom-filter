package com.khmelyuk.mbf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Component that organizes a work of multiple bloom filters.
 * It uses the same capacity for all the bloom filters, but different seeds for hash functions.
 *
 * MultiBloomFilter's {@code size} is a number of enclosed bloom filters.
 *
 * Bloom filters are reset one by one after some period of time.
 *
 * Thread safe.
 */
public class MultiBloomFilter<T> {

    private static final Logger LOG = LoggerFactory.getLogger(MultiBloomFilter.class);

    private final CircularList<BloomFilter<T>> bfs;
    private final long resetAfter;
    private long resetTime;
    private boolean autoscaling = true;

    /** Instantiates a component with {@link HashFunctions#getDefault() default hash function}. */
    public MultiBloomFilter(int filters, int capacity, Duration resetAfter, int hashes) {
        this(filters, capacity, resetAfter, hashes, HashFunctions.getDefault());
    }

    /** Instantiates a component with specified hash function. */
    @SuppressWarnings("unchecked")
    public MultiBloomFilter(int filters, int capacity, Duration resetAfter, int hashes, HashFunction<T> hashFn) {
        this.bfs = new CircularList<>(
                new BloomFilter[filters],
                (index, bf) -> {
                    int newCapacity = capacity;
                    if (bf != null && autoscaling) {
                        long updates = bf.getUpdates();
                        int oldCapacity = bf.capacity();
                        if (updates * hashes * 0.8 > oldCapacity) {
                            // increase existing capacity on 50%
                            newCapacity = (int) (oldCapacity * 1.5);
                            LOG.debug("Increasing BloomFilter size from {} to {}", oldCapacity, newCapacity);
                        }
                    }
                    return BloomFilter.create(
                            newCapacity, hashes,
                            HashFunctions.withSeed(hashFn, index));
                });
        this.resetAfter = resetAfter.toMillis();
        this.resetTime = System.currentTimeMillis();
    }

    public boolean isAutoscaling() {
        return autoscaling;
    }

    public void setAutoscaling(boolean autoscaling) {
        this.autoscaling = autoscaling;
    }

    /** Add value to the next BF */
    public void put(T value) {
        resetIfNeed();
        BloomFilter<T> bf = findBFWithoutValue(value);
        if (bf != null) {
            bf.put(value);
        }
    }

    /** Whether value found in all BFs */
    public boolean mightContain(T value) {
        resetIfNeed();
        return findBFWithoutValue(value) == null;
    }

    /** Finds BF without specified value, or null if such not found */
    private BloomFilter<T> findBFWithoutValue(T value) {
        return bfs.search((bf) -> !bf.mightContain(value));
    }

    /** Resets the head of MBF if specified period of time elapsed */
    private synchronized void resetIfNeed() {
        if (resetAfter <= 0) {
            // no need to reset if not enabled
            return;
        }

        long diff = System.currentTimeMillis() - resetTime;
        // reset as much times as need
        for (int i = 0; i < diff / resetAfter && i < bfs.size(); i++) {
            resetHead();
        }
        resetTime = System.currentTimeMillis();
    }

    void resetHead() {
        bfs.resetHead();
    }

    @Override
    public String toString() {
        return "MBF[" + bfs + "]";
    }

}
