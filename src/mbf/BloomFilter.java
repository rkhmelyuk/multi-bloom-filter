package mbf;

import java.util.BitSet;
import java.util.concurrent.atomic.AtomicLong;

/** Basic bloom filter implementation. */
public class BloomFilter<T> {

    private final BitSet set;
    private final HashFunction<T> hashFn;
    private final AtomicLong updates = new AtomicLong(0);

    public BloomFilter(int capacity) {
        this(capacity, HashFunctions.getDefault());
    }

    public BloomFilter(int capacity, HashFunction<T> hashFn) {
        this.set = new BitSet(capacity);
        this.hashFn = (value) -> Math.abs(hashFn.apply(value)) % capacity;
    }

    public void add(T value) {
        long hash = this.hashFn.apply(value);
        set.set((int) hash);
        updates.incrementAndGet();
    }

    public boolean test(T value) {
        long hash = this.hashFn.apply(value);
        return set.get((int) hash);
    }

    public Chance chance(T value) {
        if (test(value)) {
            // ok, looks like seen something
            long update = this.updates.get();
            return Chance.of((float) (this.set.size() - update) /this.set.size());
        }
        return Chance.none();
    }

}
