package com.khmelyuk.mbf;

/**
 * Represents the function that calculates a hash for input value.
 * @param <T> the input object type.
 */
public interface HashFunction<T> {

    long apply(T value);

}
