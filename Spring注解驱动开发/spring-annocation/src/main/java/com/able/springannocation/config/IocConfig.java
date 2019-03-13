package com.able.springannocation.config;

import com.able.springannocation.bean.Color;
import com.able.springannocation.bean.ColorFactoryBean;
import com.able.springannocation.bean.Person;
import com.able.springannocation.bean.Red;
import com.able.springannocation.conditional.LinuxCondition;
import com.able.springannocation.conditional.WindowsCondition;
import com.able.springannocation.selector.MyImportBeanDefinitionRegistrar;
import com.able.springannocation.selector.MyImportSelector;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;

/**
 * @author jipeng
 * @date 2019-03-01 10:47
 * @description
 */
@Configuration
//@ComponentScan(
//        value = "com.able.springannocation",
//        //指定扫描的时候按照什么规则排除那些组件
////        excludeFilters = {
////        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class, Service.class})
////        },
//        //指定扫描的时候只包含那些组件 注意 useDefaultFilters要设置为false
//        includeFilters = {
////                @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class}),
//                @ComponentScan.Filter(type = FilterType.CUSTOM,classes = {MyTypeFilter.class})
//        },useDefaultFilters = false
//)
//@Import({Color.class, Red.class, MyImportSelector.class, MyImportBeanDefinitionRegistrar.class})
public class IocConfig {
    /**
     * 方法名可以作为bean的id 也可以使用 @Bean的value属性
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    //@Lazy
    public Person person(){
        return new Person(1,"卡卡西");
    }

    @Bean("bill")
    @Conditional(WindowsCondition.class)
    public Person person1(){
        return new Person(2,"Bill Gates");
    }
    @Bean("linus")
    @Conditional(LinuxCondition.class)
    public Person person2(){
        return new Person(3,"linus");
    }
    @Bean
    public ColorFactoryBean colorFactoryBean(){
        return new ColorFactoryBean();
    }
}

