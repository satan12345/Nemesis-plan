package com.able.gcstudy;

import com.able.gcstudy.chapter2.Metaspace;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GcstudyApplicationTests {

    @Test
    public void contextLoads() {
    }

    /**
     * -Xmx32M
     * -Mms32M
     * java.lang.OutOfMemoryError: Java heap space
     */
    @Test
    public void testheap() {
        List<byte[]> lists = Lists.newArrayList();
        while (true) {
            lists.add(new byte[1024 * 1024]);
        }
    }



}
