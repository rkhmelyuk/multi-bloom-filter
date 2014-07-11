package mbf;

import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class BloomFilterTest {

    BloomFilter<String> bf = new BloomFilter<>(10);

    @Test
    public void test() throws Exception {
        bf.add("hello");
        assertTrue(bf.test("hello"));
        assertFalse(bf.test("hello2"));
    }

    @Test
    public void chance() throws Exception {
        Chance chance = bf.chance("hello");
        assertThat(chance.get(), is(0f));

        bf.add("hello");
        assertThat(bf.chance("hello").get(), is(0.98f));
    }
}