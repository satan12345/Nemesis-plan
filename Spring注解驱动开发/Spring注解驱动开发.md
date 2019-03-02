# Spring注解驱动开发

### Configuration

```java
//表名这是一个配置类
@Configuration
```

###Bean

```java
/**
     * 方法名可以作为bean的id 也可以使用 @Bean的value属性
     * @return
     */
    @Bean
    public Person person(){
        return new Person(1,"卡卡西");
    }
```

###CompontScan

```java
//bean扫描注解
@ComponentScan(
        value = "com.able.springannocation",
        //指定扫描的时候按照什么规则排除那些组件
//        excludeFilters = {
//        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class, Service.class})
//        },
        //指定扫描的时候只包含那些组件 注意 useDefaultFilters要设置为false
        includeFilters = {
//                @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = {Controller.class}),
                @ComponentScan.Filter(type = FilterType.CUSTOM,classes = {MyTypeFilter.class})
        },useDefaultFilters = false
)
```

java8中可以重复标注 @ComponentScan

低于jdk8可以使用 @ComponentScans注解 里面包含@ComponentScan数组

FilterType

```java
public enum FilterType {

	/** 按照注解
	 * Filter candidates marked with a given annotation.
	 * @see org.springframework.core.type.filter.AnnotationTypeFilter
	 */
	ANNOTATION,

	/**按照给定的类型
	 * Filter candidates assignable to a given type.
	 * @see org.springframework.core.type.filter.AssignableTypeFilter
	 */
	ASSIGNABLE_TYPE,

	/**使用 ASPECTJ
	 * Filter candidates matching a given AspectJ type pattern expression.
	 * @see org.springframework.core.type.filter.AspectJTypeFilter
	 */
	ASPECTJ,

	/**使用正则表达式
	 * Filter candidates matching a given regex pattern.
	 * @see org.springframework.core.type.filter.RegexPatternTypeFilter
	 */
	REGEX,

	/**使用自定义规则
    Filter candidates using a given custom
	 * {@link org.springframework.core.type.filter.TypeFilter} implementation.
	 */
	CUSTOM

}

```

自定义过滤规则实现类

```java
public class MyTypeFilter implements TypeFilter {

    /**
     *
     * @param metadataReader 读取到当前正在扫描的类的信息
     * @param metadataReaderFactory 可以获取到其他任何类信息的
     * @return
     * @throws IOException
     */
    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        //获取正在扫描类的注解信息
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //获取正在扫描类的类信息
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        //获取正在扫描类的资源(类的路径。。。)
        Resource resource = metadataReader.getResource();

        String className = classMetadata.getClassName();
        System.out.println("className="+ className);
        if (className.contains("er")) {
            return true;
        }
        return false;
    }
}
```

### Scope

```java
/**
	 * Specifies the name of the scope to use for the annotated component/bean.
	 * <p>Defaults to an empty string ({@code ""}) which implies
	 * {@link ConfigurableBeanFactory#SCOPE_SINGLETON SCOPE_SINGLETON}.
	 * @since 4.2
	 * @see ConfigurableBeanFactory#SCOPE_PROTOTYPE 多实例 调用的时候创建对象 获取几次调用几次
	 * @see ConfigurableBeanFactory#SCOPE_SINGLETON 单实例 ioc容器启动启动的时候会创建对象并把对象放到容器中去 
	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_REQUEST 同一次请求创建一个实例
	 * @see org.springframework.web.context.WebApplicationContext#SCOPE_SESSION 同一个session创建一个实例
	 * @see #value
	 */
```

### Lazy

```java
懒加载
 /**
     * 方法名可以作为bean的id 也可以使用 @Bean的value属性
     * @return
     */
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    @Lazy
    public Person person(){
        return new Person(1,"卡卡西");
    }
```

###Conditional

按照一定的条件判断 进行判断 满足条件给容器中注册bean

可以放在类上  放在类上 只有满足条件 类中的组件才会注册

也可以放在方法上

```java
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
public class LinuxCondition implements Condition {
    /**
     *
     * @param context 判断条件上下文环境
     * @param metadata 注解信息
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //获取当前的系统环境
        Environment environment = context.getEnvironment();
        String property = environment.getProperty("os.name");
        System.out.println("LinuxCondition="+property);
        //获取beanRegistry bean定义的注册类 可以判断容器中bean的注册情况 也可以给容器中注册bean
        BeanDefinitionRegistry registry = context.getRegistry();
        //获取BeanFactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        //获取类加载器
        ClassLoader classLoader = context.getClassLoader();
        if (property.contains("lin")) {
            return true;
        }
        return false;
    }
}
public class WindowsCondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {

        String property = context.getEnvironment().getProperty("os.name");
        System.out.println("WindowsCondition="+property);
        if (property.toLowerCase().contains("win")) {
            return true;
        }
        return false;
    }
}


```

###Import

给容器中注册组件:

 1.  包扫描+组件标注注解(@Controller @Service @Repository @Component  (通常自己写的组件)

 2.  @Bean【导入第三方包里的组件】

 3.  @Import [快速给容器中导入组件]

      	1. @import(要导入到容器中的组件):容器中就会自动注册这个组件 id默认是全类名
      	2. ImportSelector:返回需要导入的组件全类名数组
      	3. ImportBeanDefinitionRegistrar:手工注册bean到容器中

	4. 使用Spring提供的FactoryBean

    	1. 默认获取到的工厂bean调用getObject创建对象
    	2. 要获取工厂Bean本身 我们需要给id前面添加一个&

    ```java
    //@Import导入组件 id默认是组件的全类名
    @Import({Color.class, Red.class, MyImportSelector.class})
    ```

    ```java
    public class MyImportSelector implements ImportSelector {
        /**
         *
         * @param importingClassMetadata 当前标注@Import注解的类的所有注解信息
         * @return
         */
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {

            return new String[]{Green.class.getName(), Bule.class.getName()};
        }
    }
    ```

    ```java
    @Slf4j
    public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
        /**
         *
         * @param importingClassMetadata 当前类的注解信息
         * @param registry bean定义的注册类
         */
        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            
            RootBeanDefinition beanDefinition=new RootBeanDefinition(RainBow.class);
            MutablePropertyValues propertyValues=new MutablePropertyValues();
            propertyValues.add("number",88);
            beanDefinition.setPropertyValues(propertyValues);
            registry.registerBeanDefinition("rainBow",beanDefinition);
        }
    }
    ```

    ###

```java
 * bean的生命周期：
 *  bean创建--初始化--消耗过程
 *  容器管理bean的生命周期:
 *     我们可以自定义初始化和消耗方法：容器在bean进行到当前生命周期的时候来调用我们自定义的初始化和销毁方法
 *     1 指定初始化和销毁的方法:
 *      指定 init-method 和destory-method
 *     2 bean实现 InitializingBean DisposableBean 接口
 *     3 可以使用Jsr250的注解 PostConstruct PreDestroy
 *		4 BeanPostProcessor:bean的后置处理器
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
```

```java
public class Car implements InitializingBean, DisposableBean {

    public Car() {
        System.out.println("car================constructor");
    }
    //对象创建并赋值之后调用
    @PostConstruct
    public void postConstrctor(){
        System.out.println("car========postConstructor========");
    }

    @PreDestroy
    public void preDestory(){
        System.out.println("car======preDestory========");
    }
    public void  init(){
        System.out.println("car===================init=======");
    }
    public void destory(){
        System.out.println("car====================destory");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("car====================destroy");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("car=================afterPropertiesSet");
    }


}

@Configuration
@ComponentScan(basePackages = {
        "com.able.springannocation.processor"
})
public class BeanCycleConfig {
    @Bean(initMethod ="init",destroyMethod = "destory")
    public Car car(){
        return new Car();
    }
}

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(beanName+"================postProcessBeforeInitialization");
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println(beanName+"================postProcessAfterInitialization");
        return bean;
    }
}

```

### Value

​	propertySource

​	propertySources

```java
@Configuration
@PropertySource({
        "classpath:application.properties"
})
public class PropertyConfig {
    @Bean
    public Person person(){

        return new Person();

    }
}
@Data
public class Person {
    /**
     * 使用@Value 赋值
     * 1 基本数值
     * 2 可以使用Spel:#{}
     * 3 可以使用${},使用配置文件中的值
     *
     */
    @Value("#{20-2}")
    private Integer id;

    /**
    *
    */
    @Value("张三")
    private String name;
    @Value("${nike.name}")
    private String nikeName;

    public Person() {

    }

    public Person(Integer id, String name) {
        this.id = id;
        this.name = name;
        //System.out.println("person创建");
    }
}
```

### 自动注入

```java
/* *      spring利用DI 完成对IOC容器中的各个组件的依赖关系赋值
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
 
 **/
```



```java
*@Autowired :
*         标注在方法上
*         标注在构造器上:如果组件只有一个有参构造器  @Autowired可以省略
*         标注在参数上
@Component
public class Boss {


    Car car;

    @Autowired
    public Boss(@Autowired Car car) {
        this.car = car;
    }


    public Car getCar() {
        return car;
    }
    @Autowired
    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Boss{" +
                "car=" + car +
                '}';
    }
}
```

自定义组件 想要使用Spring容器底层的一些组件(ApplicationContext BeanFactory xxx)

​	自定义组件实现xxxAware: 在创建对象的时候 会调用接口规定的方法注入相关组件：Aware



