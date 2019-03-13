package com.able.springannocation.config;

import com.able.springannocation.bean.Car;
import com.able.springannocation.bean.Color;
import com.able.springannocation.bean.Dog;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author jipeng
 * @date 2019-03-02 15:27
 * @description
 * 自动装配：
 *      spring利用DI 完成对IOC容器中的各个组件的依赖关系赋值
 *       1 @Autowired
 *             1 默认按照类型取容器中找对应的组件
 *             2 如果找到多个相同的组件 在将属性的名称作为组件的id去容器中查找
 *             3 使用@Qualifier()指定要装配的组件id 而不是使用属性名
 *             4 自动装配 默认一定要将属性赋值好 没有则会报错
 *               可以使用 @Autowired(required=false) 来装配 有就装配 没有也不报错
 *             5 @Primary ：让Spring进行装配的时候默认使用首选的bean
 *                  也可以继续使用@Qualifier 指定需要装配的bean的名字
 *
 *       2 @Resource(Jsr250) [java规范注解]
 *              可以和@Autowired一样事项自动装配功能 默认是按照组件名称进行装配的
 *               没有能支持@Primary功能
 *               没有能支持@Autowired(required=false)的功能
 *              @Resource(name="xxx")
 *       3 @Inject(Jsr 330)  [java规范注解]
 *              需要导入javax.inject的包  和autowired功能一样 不同的是不支持required属性
 *         AutowiredAnnotationBeanPostProcessor:解析完成自动装配的功能
 *
 *
 *     @Autowired :
 *         标注在方法上
 *         标注在构造器上:如果组件只有一个有参构造器  @Autowired可以省略
 *         标注在参数上
 *
 *
 *
 *
 *
 */
//@Configuration
//@ComponentScan(basePackages = {"com.able.springannocation.bean"})
public class AutoWiredConfig {

    @Bean
    public Color color(Car car){
       Color color=new Color();
       color.setCar(car);
       return color;
    }
    @Bean
    public Dog dog(){
        return new Dog();
    }
}

