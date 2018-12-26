package com.able.lianshugc;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LianshugcApplicationTests {

    Runtime runtime;

    @Before
    public void init() {
        runtime = Runtime.getRuntime();
        //系统最大可以分配的内存
        log.info("maxMemory={}", runtime.maxMemory() / 1024 / 1024 + "M");
        //系统的空闲内存
        log.info("freeMemory={}", runtime.freeMemory() / 1024 / 1024 + "M");
        //系统暂时分配到的内存大小
        log.info("totalMemory={}", runtime.totalMemory() / 1024 / 1024 + "M");
    }

    /**
     * -Xmx64M
     * -Xms64M
     */
    @Test
    public void testMemory() {

        /**
         maxMemory=58M
         freeMemory=37M
         totalMemory=58M
         */
    }

    /**
     * -Xmx32M -Xms32M
     */
    @Test
    public void testUserMemory() {
        byte[] bs = new byte[1024 * 1024];
        log.info("系统分配了1M的空间");
        /**
         *  maxMemory=29M
         freeMemory=15M
         totalMemory=29M
         系统分配了1M的空间
         maxMemory=29M
         freeMemory=14M
         totalMemory=29M
         系统分配了1M内存创建对象 所以可用的内存也就较少了1M
         */


    }

    /**
     * -Xmx16M -Xms5M
     */
    @Test
    public void testUserMemory1() {
        byte[] bs = new byte[6 * 1024 * 1024];
        log.info("系统分配了6M的空间");
        /**
         maxMemory=14M
         freeMemory=4M
         totalMemory=11M
         系统分配了1M的空间
         maxMemory=14M
         freeMemory=2M
         totalMemory=14M
         刚开始系统有4M可用内存 不够用于分配 因此进行扩容 因此前后系统可用的内存大小由11M变成14M
         */
    }

    /**
     * -Xmx20M
     * -Xms20M
     * -Xmn4M
     * -XX:+PrintGCDetails
     * -XX:+UseSerialGC
     * -XX:SurvivorRatio=8
     */
    @Test
    public void testUseMemory2 (){
        byte[] b;
        for (int i = 0; i < 10; i++) {
            b=new byte[1024*1024];
        }
    }
    /**
     * -Xmx20M
     * -Xms20M
     * -XX:+HeapDumpOnOutOfMemoryError
     * -XX:+HeapDumpPath=e:/error.dump
     */
    @Test
    public void testHeadDumpTest (){
        List<byte[]> list= Lists.newArrayList();
        for (int i = 0; i < 30; i++) {
            list.add(new byte[1024*1024]);
        }
    }



    @After
    public void destory() {
        runtime = Runtime.getRuntime();
        //系统最大可以分配的内存
        log.info("maxMemory={}", runtime.maxMemory() / 1024 / 1024 + "M");
        //系统的空闲内存
        log.info("freeMemory={}", runtime.freeMemory() / 1024 / 1024 + "M");
        //系统暂时分配到的内存大小
        log.info("totalMemory={}", runtime.totalMemory() / 1024 / 1024 + "M");
    }

}

