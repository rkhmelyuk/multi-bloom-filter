package com.khmelyuk.mbf;

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

    /** Instantiates a component with {@link HashFunctions#getDefault() default hash function}. */
    public MultiBloomFilter(int size, int capacity, int hashes) {
        this(size, capacity, hashes, HashFunctions.getDefault());
    }

    /** Instantiates a component with specified hash function. */
    @SuppressWarnings("unchecked")
    public MultiBloomFilter(int filters, int capacity, int hashes, HashFunction<T> hashFn) {
        this.bfs = new CircularList<>(
                new BloomFilter[filters],
                (index) -> BloomFilter.create(
                        capacity, hashes,
                        HashFunctions.withSeed(hashFn, index)));
    }

    /** Add value to the next BF */
    public void put(T value) {
        BloomFilter<T> bf = findBFWithoutValue(value);
        if (bf != null) {
            bf.put(value);
        }
    }

    /** Whether value found in all BFs */
    public boolean mightContain(T value) {
        return findBFWithoutValue(value) == null;
    }

    /** Finds BF without specified value, or null if such not found */
    private BloomFilter<T> findBFWithoutValue(T value) {
        return bfs.find((bf) -> !bf.mightContain(value));
    }

    void resetHead() {
        bfs.resetHead();
    }

    @Override
    public String toString() {
        return "MBF[" + bfs + "]";
    }

}
