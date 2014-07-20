package mbf;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BloomFilterTest {

    @Test
    public void multipleHashes() throws Exception {
        BloomFilter<String> bf = BloomFilter.create(10, 3);
        bf.put("hello");
        bf.put("hello");
        assertTrue(bf.mightContain("hello"));
        assertFalse(bf.mightContain("hello2"));
    }

    @Test
    public void singleHash() throws Exception {
        BloomFilter<String> bf = BloomFilter.create(10, 1);
        bf.put("hello");
        bf.put("hello");
        assertTrue(bf.mightContain("hello"));
        assertFalse(bf.mightContain("hello2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void noHashFunction() throws Exception {
        BloomFilter.create(10, 1, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroCapacity() throws Exception {
        BloomFilter.create(0, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeCapacity() throws Exception {
        BloomFilter.create(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void zeroHashes() throws Exception {
        BloomFilter.create(10, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void negativeHashes() throws Exception {
        BloomFilter.create(10, -1);
    }
}