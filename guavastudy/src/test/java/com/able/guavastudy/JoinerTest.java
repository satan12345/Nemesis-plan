package com.able.guavastudy;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author jipeng
 * @date 2018-12-25 20:03
 * @description
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JoinerTest {
    List<String> stringList = Arrays.asList("Google", "Guava", "Java", "Scale", "Kafka");
    List<String> stringListWithNullValue = Arrays.asList("Google", "Guava", "Scala", null);

    @Test
    public void testJoinOnJoin() {
        String join = Joiner.on("#").join(stringList);
        log.info("join={}", join);

    }

    @Test
    public void testJoinWithNullValue() {
        String join = Joiner.on(",").skipNulls().join(stringListWithNullValue);
        log.info("join={}", join);
    }

    @Test
    public void testJoinWithNullDefault() {
        String aDefault = Joiner.on(".").useForNull("default").join(stringListWithNullValue);
        log.info("default={}", aDefault);
    }

    @Test
    public void testJoinOnAppendToStringBuilder() {

        StringBuilder stringBuilder1 = Joiner.on("#").useForNull("defalut").appendTo(new StringBuilder(), stringListWithNullValue);
        log.info("stringBuilder1={}", stringBuilder1);
    }

    /**
     *
     */
    private String fileName = "D:\\BaiduNetdiskDownload\\汪文君Google Guava实战视频\\guava-joiner.txt";

    @Test
    public void testAppendToWriter() {
        try (FileWriter fileWriter = new FileWriter(new File(fileName))) {
            Joiner.on(".").useForNull("xxx").appendTo(fileWriter,stringListWithNullValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        boolean test = Files.isFile().test(new File(fileName));
        log.info("文件是否存在:{}",test);

    }
    @Test
    public void testJoinWithStream (){
        stringListWithNullValue.stream().filter(x->x!=null&&!x.isEmpty()).collect(Collectors.joining("$"));
    }

    Map<String,String> map= ImmutableMap.of("hello","world","java","python");
    @Test
    public void testTestJoinWithMap (){
        String join = Joiner.on(",").withKeyValueSeparator("=").join(map);
        log.info("join:{}",join);

    }

}

