package com.khmelyuk.mbf;

import java.time.Duration;

/**
 * Component that organizes a work of multiple bloom filters.
 * It uses the same capacity for all the bloom filters, but different seeds for hash functions.
 *
 * MultiBloomFilter's {@code size} is a number of enclose bloom filters.
 *
 * Bloom filters are reset one by one after some period of time.
 */
// TODO - concurrency support...
public class MultiBloomFilter<T> {

    private final CircularList<BloomFilter<T>> bfs;
    private final long resetAfter;
    private long resetTime;

    /** Instantiates a component with {@link HashFunctions#getDefault() default hash function}. */
    public MultiBloomFilter(int filters, int capacity, Duration resetAfter, int hashes) {
        this(filters, capacity, resetAfter, hashes, HashFunctions.getDefault());
    }

    /** Instantiates a component with specified hash function. */
    @SuppressWarnings("unchecked")
    public MultiBloomFilter(int filters, int capacity, Duration resetAfter, int hashes, HashFunction<T> hashFn) {
        this.bfs = new CircularList<>(
                new BloomFilter[filters],
                (index) -> BloomFilter.create(
                        capacity, hashes,
                        HashFunctions.withSeed(hashFn, index)));
        this.resetAfter = resetAfter.toMillis();
        this.resetTime = System.currentTimeMillis();
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
    private void resetIfNeed() {
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

    void resetHead() {bfs.resetHead();}

    @Override
    public String toString() {
        return "MBF[" + bfs + "]";
    }

}
