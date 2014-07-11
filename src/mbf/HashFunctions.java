package mbf;

/** Contains the most widely used type of hash functions. */
public class HashFunctions {

    // TODO - add more hash functions

    /** Java language hashCode */
    public static <T> HashFunction<T> getDefault() {
        return Object::hashCode;
    }

}
