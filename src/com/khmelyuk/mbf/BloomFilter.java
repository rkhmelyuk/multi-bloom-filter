package com.khmelyuk.mbf;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Basic bloom filter implementation.
 * Primary attributes are:
 * - capacity: size of the bloom filter
 * - hashes: how many time the hash functions would be run over the input value
 * - hash function: the hash function to run for the in put value
 *
 * The hash function would be run n times for some input value, but would have different seed value,
 * so results would be different. See {@link com.khmelyuk.mbf.HashFunctions#withSeed(HashFunction, int)} for more info on this.
 */
public class BloomFilter<T> {

    public static <T> BloomFilter<T> create(int capacity, int hashes) {
        return create(capacity, hashes, HashFunctions.getDefault());
    }

    public static <T> BloomFilter<T> create(int capacity, int hashes, HashFunction<T> hashFn) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity is negative or zero");
        }
        if (hashes <= 0) {
            throw new IllegalArgumentException("number of hashes is negative or zero");
        }
        if (hashFn == null) {
            throw new IllegalArgumentException("hash function is required");
        }

        return new BloomFilter<>(capacity, hashes, hashFn);
    }

    // --------------------- implementation

    private final BitSet set;
    private final HashFunction<T> hashFn;
    private final AtomicLong updates = new AtomicLong(0);
    private final int hashes;

    private BloomFilter(int capacity, int hashes, HashFunction<T> hashFn) {
        this.set = new BitSet(capacity);

        this.hashFn = (value) -> Math.abs(hashFn.apply(value)) % capacity;
        this.hashes = hashes;
    }

    /** Put value into this bloom filter */
    public void put(T value) {
        for (int i = 0; i < hashes; i++) {
            long hash = HashFunctions.withSeed(this.hashFn, i).apply(value);
            set.set((int) hash);
        }
        updates.incrementAndGet();
    }

    /** Check if the bloom filter might contain the value */
    public boolean mightContain(T value) {
        for (int i = 0; i < hashes; i++) {
            long hash = HashFunctions.withSeed(this.hashFn, i).apply(value);
            if (!set.get((int) hash)) {
                return false;
            }
        }
        return true;
    }

    public String toString() {
        return set.toString();
    }

}
