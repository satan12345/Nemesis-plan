package com.able.springannocation.config;

import com.able.springannocation.bean.Car;
import com.able.springannocation.bean.Dog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

/**
 * @author jipeng
 * @date 2019-03-01 16:49
 * @description
 * bean的生命周期：
 *  bean创建--初始化--消耗过程
 *  容器管理bean的生命周期:
 *     我们可以自定义初始化和消耗方法：容器在bean进行到当前生命周期的时候来调用我们自定义的初始化和销毁方法
 *     1 指定初始化和销毁的方法:
 *      指定 init-method 和destory-method
 *     2 bean实现 InitializingBean DisposableBean 接口
 *     3 可以使用Jsr250的注解 PostConstruct PreDestroy
 *     4 BeanPostProcessor:bean的后置处理器
 *      在bean初始化前后进行一系列处理工作
 *
 *    构造:创建对象
 *      单实例：在容器启动的时候创建对象
 *      多实例：在每次获取的时候创建对象
 *    BeanPostProcessor.postProcessBeforeInitialization
 *    初始化：
 *      对象完成 并赋值好 调用初始化方
 *    BeanPostProcessor.postProcessAfterInitialization
 *    销毁：
 *      单实例：容器关闭的时候
 *      多实例：容器不会管理这个bean 不会调用销毁方法
 *
 */
@Configuration
@ComponentScan(basePackages = {
        "com.able.springannocation.processor"
})
public class BeanCycleConfig {
    @Bean(initMethod ="init",destroyMethod = "destory")
    public Car car(){
        return new Car();
    }
    @Bean
    public Dog dog(){
        return new Dog();
    }
}

