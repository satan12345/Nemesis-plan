package com.able.guavastudy;

import com.google.common.base.Splitter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author jipeng
 * @date 2018-12-26 10:40
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SplitterTest {

    @Test
    public void testSplitOnSplit() {
        List<String> list = Splitter.on("|").splitToList("鸣人|卡卡西");
        log.info("list={}",list);
    }

    /**
     * 忽略空字符串
     */
    @Test
    public void testSplitOnSplitOMitEmpty (){
        Iterable<String> split = Splitter.on('|').omitEmptyStrings().splitToList(" 鸣人|卡卡西||小樱");
        split.forEach(System.out::println);
    }
    @Test
    public void testSplitOmitEmptyTrinResult (){
        Iterable<String> split = Splitter.on('|').omitEmptyStrings().trimResults().splitToList(" 鸣人 | 卡卡西 ||小樱");
        split.forEach(System.out::println);
    }
    @Test
    public void testSplitFixtLength (){
        Iterable<String> aabbccdd = Splitter.fixedLength(2).splitToList("AABBCCDD");
        aabbccdd.forEach(System.out::println);
    }
    @Test
    public void testSplitOnlimit (){
        Iterable<String> split = Splitter.on("#").omitEmptyStrings().limit(3).split("今#天#天#气#好#么#");
        split.forEach(System.out::println);
    }
    @Test
    public void testSplitToMap (){
        Map<String, String> split = Splitter.on("#").trimResults().omitEmptyStrings().withKeyValueSeparator("=").split("影=卡卡西# 下忍 = 鸣人");
        for (Map.Entry<String, String> stringStringEntry : split.entrySet()) {
            log.info("stringStringEntry.key={}",stringStringEntry.getKey());
            log.info("stringStringEntry.value={}",stringStringEntry.getValue());
        }
    }
}

