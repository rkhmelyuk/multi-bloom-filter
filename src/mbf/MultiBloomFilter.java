package mbf;

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

    // TODO - structure that represents a CircularList

    private final BloomFilter<T>[] bfs;
    private final HashFunction<T> hashFn;
    private final int capacity;
    private final int hashes;
    private int head;

    /** Instantiates a component with {@link HashFunctions#getDefault() default hash function}. */
    public MultiBloomFilter(int size, int capacity, int hashes) {
        this(size, capacity, hashes, HashFunctions.getDefault());
    }

    /** Instantiates a component with specified hash function. */
    @SuppressWarnings("unchecked")
    public MultiBloomFilter(int filters, int capacity, int hashes, HashFunction<T> hashFn) {
        this.head = 0;
        this.hashes = hashes;
        this.hashFn = hashFn;
        this.capacity = capacity;
        this.bfs = new BloomFilter[filters];
        for (int i = 0; i < filters; i++) {
            this.bfs[i] = BloomFilter.create(capacity, hashes, HashFunctions.withSeed(hashFn, i));
        }
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
        // use some kind of binary search here
        int head = this.head;
        int tail = (head == 0 ? this.bfs.length - 1 : head - 1);

        // TODO - use binary search?
        for (int i = head; i < this.bfs.length; i++) {
            if (!bfs[i].mightContain(value)) {
                return bfs[i];
            }
        }
        for (int i = tail; i < head; i++) {
            if (!bfs[i].mightContain(value)) {
                return bfs[i];
            }
        }
        return null;
    }

    void resetHead() {
        int head = this.head;

        BloomFilter<T> newBF = BloomFilter.create(capacity, hashes, HashFunctions.withSeed(hashFn, head));
        int newHead = (head == this.bfs.length - 1 ? 0 : head + 1);

        bfs[head] = newBF;
        this.head = newHead;
    }

}
