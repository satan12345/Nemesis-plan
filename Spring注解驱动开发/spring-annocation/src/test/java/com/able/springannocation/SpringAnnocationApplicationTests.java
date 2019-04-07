package com.able.springannocation;

import com.able.springannocation.bean.Person;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringAnnocationApplicationTests {
    @Resource
    ApplicationContext applicationContext;

    @Test
    public void contextLoads() {
        Person bean = applicationContext.getBean(Person.class);
        log.info("bean={}",bean);
        String[] beanNamesForType = applicationContext.getBeanNamesForType(Person.class);
        Stream.of(beanNamesForType).forEach(System.out::println);

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            log.info("name= {}",beanDefinitionName);
        }
    }
    @Test
    public void testScope(){
        Person person1 = applicationContext.getBean(Person.class);
        Person person2 = applicationContext.getBean(Person.class);
        System.out.println(person1==person2);
    }
    @Test
    public void testLasy_Loading(){
//        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
//        for (String name : beanDefinitionNames) {
//            System.out.println(name);
//        }
//        System.out.println("======开始获取bean");
//        Person bean = applicationContext.getBean(Person.class);
//        Person bean1 = applicationContext.getBean(Person.class);
//        System.out.println("bean1==bean2"+(bean==bean1));

        String bySqlQuery = findBySqlQuery(null,
                1,
                null,
                null,
                null,
                null,
                null,
                "PUSH_TIME=DESC"
        );
        System.out.println(bySqlQuery);

    }

    public static void main(String[] args){

    }
    private static String findBySqlQuery(Long id, Integer type,
                                            Integer status,
                                            Integer startPage,
                                            Integer pageSize,
                                            Date pushTimeStart,
                                            Date pushTimeEnd,
                                            String orderMode) {

        List<Object> params = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder("SELECT art.ID, art.TITLE, art.IMAGE, art.BRIEF, art.CONTENT, art.LINK, art.TYPE, art.`STATUS`, art.PUSH_TIME, art.CREATE_TIME, art.UPDATE_TIME, art.IS_DELETE, SUM(CASE WHEN stu_art.TYPE = 1 THEN 1 ELSE 0 END) READ_COUNT, SUM(CASE WHEN stu_art.TYPE = 2 THEN 1 ELSE 0 END) STAR_COUNT FROM TBL_ARTICLE art LEFT JOIN STUDENT_ARTICLE stu_art ON stu_art.ARTICLE_ID = art.ID AND stu_art.IS_DELETE = 0 WHERE 1 = 1");
        // ID匹配查找
        if (id != null) {
            sql.append(" AND art.ID = ?");
            params.add(id);
        }
        // 类型（1每日新鲜事）
        if (type != null) {
            sql.append(" AND art.TYPE = ? ");
            params.add(type);
        }
        // 发布状态
        if (status != null) {
            //未发布的过滤定时未推送的
            if (status == 0) {
                sql.append(" AND (art.`STATUS` = 0 AND art.PUSH_TIME >= NOW()) ");
            }
            //已发布的包含定时未推送的
            if (status == 1) {
                sql.append(" AND (art.`STATUS` = 1 OR (art.`STATUS` = 0 AND art.PUSH_TIME <= NOW())) ");
            }
            //其他新增状态情况
        }
        // 推送起始时间
        if (pushTimeStart != null) {
            sql.append(" AND art.PUSH_TIME >= ? ");
            params.add(pushTimeStart);
        }
        // 推送截止时间
        if (pushTimeEnd != null) {
            sql.append(" AND art.PUSH_TIME <= ? ");
            params.add(pushTimeEnd);
        }
        sql.append(" AND art.IS_DELETE = 0 ");
        // 点赞量 阅读量 分组计算
        sql.append(" GROUP BY art.ID ");

        if (!StringUtils.isEmpty(orderMode)) {
            String[] split = StringUtils.split(orderMode, ",");
            for (String s : split) {
                String[] order = StringUtils.split(s, "=");
                sql.append(" ORDER BY ").append(order[0]).append(" ")
                        .append(order[1] == null ? "ASC" : order[1]).append(" ");
            }
        }
        if (startPage != null && startPage > 0 && pageSize != null && pageSize > 0) {
            int offset = (startPage - 1) * pageSize;
            sql.append(" LIMIT ?, ? ");
            params.add(offset);
            params.add(pageSize);
        }
        return sql.toString();

    }


}
