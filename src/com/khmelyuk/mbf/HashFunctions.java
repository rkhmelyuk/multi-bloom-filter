package com.khmelyuk.mbf;

import com.google.common.hash.Hashing;

/** Contains the most widely used type of hash functions. */
public class HashFunctions {

    public static <T> HashFunction<T> getDefault() {
        return murmur128();
    }

    public static <T> HashFunction<T> murmur128() {
        return (value) -> Hashing.murmur3_128().hashObject(value, new UniversalFunnel()).asLong();
    }

    public static <T> HashFunction<T> adler32() {
        return (value) -> Hashing.adler32().hashObject(value, new UniversalFunnel()).asLong();
    }

    public static <T> HashFunction<T> md5() {
        return (value) -> Hashing.md5().hashObject(value, new UniversalFunnel()).asLong();
    }

    /** Returns the hash function that adds a seed value for the input hash function. */
    public static <T> HashFunction<T> withSeed(HashFunction<T> hashFn, int seed) {
        return (value) -> hashFn.apply(value) + seed << 2;
    }

}
