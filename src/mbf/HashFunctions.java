package mbf;

/** Contains the most widely used type of hash functions. */
public class HashFunctions {

    // TODO - add more hash functions

    /** Java language hashCode */
    public static <T> HashFunction<T> getDefault() {
        return Object::hashCode;
    }

    public static <T> HashFunction<T> murmur128() {
        throw new UnsupportedOperationException("implement me");
    }

    /** Returns the hash function that adds a seed value for the input hash function. */
    public static <T> HashFunction<T> withSeed(HashFunction<T> hashFn, int seed) {
        return (value) -> hashFn.apply(value) + seed << 2;
    }

}
