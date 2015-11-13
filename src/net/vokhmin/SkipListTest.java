package net.vokhmin;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SkipListTest {

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @Test
    public void testA() {
        Map<Integer, String> data = new LinkedHashMap<>();
        Integer[] keys = {2, 4, 5, 9, 15, 19, 28, 33, 42};
        List<Integer> shuffle =  Arrays.asList(keys);
        Collections.shuffle(shuffle);
        for (Integer k : keys) {
            data.put(k, "" + k);
        }

        SkipList<Integer, String> list = new SkipList<Integer, String>(Integer.MAX_VALUE);
        assertEquals(0, list.size());
        for (Integer k : shuffle) {
            assertNull(list.find(k));
        }

        for (Map.Entry<Integer, String> e : data.entrySet()) {
            list.insert(e.getKey(), e.getValue());
        }
        assertEquals(shuffle.size(), list.size());
        for (Integer k : shuffle) {
            assertNotNull(list.find(k));
        }

        for (Integer k : shuffle) {
            list.remove(k);
            assertNull(list.find(k));
        }
        assertEquals(0, list.size());

    }
}