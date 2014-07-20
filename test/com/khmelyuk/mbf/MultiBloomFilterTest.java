package com.khmelyuk.mbf;

import org.junit.Test;

import java.time.Duration;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MultiBloomFilterTest {

    MultiBloomFilter<String> mbf = new MultiBloomFilter<>(3, 10, Duration.ofMillis(1000), 3);

    @Test
    public void putSingle() throws Exception {
        mbf.put("hello");
        assertFalse(mbf.mightContain("hello"));
    }

    @Test
    public void putMultiple() throws Exception {
        mbf.put("hello");
        mbf.put("hello");
        assertFalse(mbf.mightContain("hello"));
    }

    @Test
    public void putAll() throws Exception {
        mbf.put("hello");
        mbf.put("hello");
        mbf.put("hello");
        assertTrue(mbf.mightContain("hello"));
    }

    @Test
    public void putThenResetThenPutAgain() throws Exception {
        mbf.put("hello");
        mbf.put("hello");
        mbf.resetHead();
        mbf.put("hello");
        assertFalse(mbf.mightContain("hello"));

        mbf.put("hello");
        assertTrue(mbf.mightContain("hello"));
        mbf.resetHead();
        assertFalse(mbf.mightContain("hello"));
    }

    @Test
    public void multipleHeadResetsIsImportant() throws Exception {
        mbf.put("hello");
        mbf.put("hello");
        mbf.resetHead();
        mbf.resetHead();
        mbf.put("hello");
        assertFalse(mbf.mightContain("hello"));
        mbf.put("hello");
        assertFalse(mbf.mightContain("hello"));
        mbf.put("hello");
        assertTrue(mbf.mightContain("hello"));
    }

    @Test
    public void resetAfterTime() throws Exception {
        mbf = new MultiBloomFilter<>(3, 10, Duration.ofMillis(50), 3);
        mbf.put("hello");
        mbf.put("hello");
        mbf.put("hello");
        assertTrue(mbf.mightContain("hello"));
        Thread.sleep(60);
        assertFalse(mbf.mightContain("hello"));
        mbf.put("hello");
        assertTrue(mbf.mightContain("hello"));

        Thread.sleep(175);
        assertFalse(mbf.mightContain("hello"));
        mbf.put("hello");
        assertFalse(mbf.mightContain("hello"));
        mbf.put("hello");
        assertFalse(mbf.mightContain("hello"));
        mbf.put("hello");
        assertTrue(mbf.mightContain("hello"));
    }

    @Test
    public void resetAfterDisabled() throws Exception {
        mbf = new MultiBloomFilter<>(3, 10, Duration.ofMillis(-1), 3);
        mbf.put("hello");
        mbf.put("hello");
        mbf.put("hello");
        assertTrue(mbf.mightContain("hello"));
        Thread.sleep(50);
        assertTrue(mbf.mightContain("hello"));
    }
}