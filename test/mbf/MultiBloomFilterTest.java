package mbf;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MultiBloomFilterTest {

    MultiBloomFilter<String> mbf = new MultiBloomFilter<>(3, 10, 3);

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
}