package com.able;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author jipeng
 * @date 2019-01-18 10:32
 * @description
 */
public class HashMapTest {
    @Test

    public void testHashMap() {
        Map<Integer, Integer> map = new HashMap();
        for (int i = 0; i < 10; i++) {
            map.put(i, i);
        }



    }
}

