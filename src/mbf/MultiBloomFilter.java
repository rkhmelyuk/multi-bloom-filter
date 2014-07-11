package mbf;

import java.util.Arrays;

/** TODO - javadoc */
public class MultiBloomFilter<T> {

    private final BloomFilter[] bfs;

    public MultiBloomFilter(int filters, int baseCapacity) {
        this.bfs = new BloomFilter[filters];
        for (int i = 0; i < filters; i++) {
            this.bfs[i] = new BloomFilter<>(baseCapacity + (int) Math.max(i << 2, baseCapacity * 0.4));
        }
    }

    /** Add value to the next BF */
    public void add(T value) {
        BloomFilter bf = findBFWithoutValue(value);
    }

    /** Whether value found in all BFs */
    public boolean test(T value) {
        return findBFWithoutValue(value) == null;
    }

    /** Finds BF without specified value, or null if such not found */
    private BloomFilter findBFWithoutValue(T value) {
        // use some kind of binary search here
        if (!bfs[0].test(value)) {
            return bfs[0];
        }
    }

}
