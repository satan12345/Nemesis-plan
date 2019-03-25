package com.able.springboothelloworld;

import com.able.springboothelloworld.com.able.model.Person;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Slf4j
@SpringBootApplication
@PropertySources({
        @PropertySource("")
})
public class SpringbootHelloworldApplication implements CommandLineRunner {

    @Resource
    ApplicationContext applicationContext;
    public static void main(String[] args) {
        SpringApplication.run(SpringbootHelloworldApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        int count = applicationContext.getBeanDefinitionCount();
        log.info("count={}",count);
        log.error("这是一个错误消息");
        log.warn("这是一条警告消息");
        log.info("这是一个info输出");
        log.debug("这是一条debug日志");
        log.trace("这是一条trace信息");

        BigDecimal bigDecimal=new BigDecimal(Integer.valueOf(0));
        System.out.println(bigDecimal.equals(BigDecimal.ZERO));

    }
}
