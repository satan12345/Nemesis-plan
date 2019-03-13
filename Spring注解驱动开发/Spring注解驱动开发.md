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
    
       2. 使用Spring提供的FactoryBean
    
         1. 默认获取到的工厂bean调用getObject创建对象
         2. 要获取工厂Bean本身 我们需要给id前面添加一个&
    
    ​```java
    //@Import导入组件 id默认是组件的全类名
    @Import({Color.class, Red.class, MyImportSelector.class})
    ​```
    
    ​```java
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
    ​```
    
    ​```java
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
    ​```
    
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

​	把Spring底层的一些组件注入到自定义组件中

​	xxxAware:功能使用xxxProcessor

​		applicationConextAware===>ApplicationContextAwareProcessor

### Profile

Spring为我们提供可以根据当前环境 动态激活和切换一些列组件的功能:

指定组件在哪个环境的情况下才能被注册到容器中 不指定 任何环境都能注册到这个组件

加了环境标识的bean 只有这个环境被激活的时候才能注册到容器中  默认是default

写在配置类上 只有指定环境的时候 整个配置类里面的所有配置才能开始生效

@Profile("dev")

@Profile("prod")



```java
@Configuration
public class ProfileConfig {

    @Bean
    @Profile("default")
    public Car car(){
        return new Car();
    }

    @Bean
    @Profile("dev")
    public Bule bule(){
        return new Bule();
    }


}
```

使用命令行动态参数：

-Dspring.profiles.active=dev

使用代码

```java
   @Before
    public void  init(){
       // applicationContext=new AnnotationConfigApplicationContext(ProfileConfig.class);
        //创建 applicationContext
        applicationContext=new AnnotationConfigApplicationContext();
        //设置需要激活的环境
        applicationContext.getEnvironment().setActiveProfiles("dev");
        //注册主配置类
        applicationContext.register(ProfileConfig.class);
        //刷新启动容器
        applicationContext.refresh();
    }
```

### AOP

​	动态代理

指在程序运行期间动态的将某段代码切入到指定方法的指定位置进行运行的编程方式

​	1.导入aop模块:spring-aop:(spring-aspect)

​	2.定义一个业务逻辑类(MatchCalculaotr):在业务逻辑运行的时候将日志进行打印（方法之前 方法运行结束 方法出现异常）

```java
public class MathCalculator {
}

```



​	3 定义一个日志切面类 切面里的方法需要动态感知业务运行到哪里 然后执行

​			通知方法：

			>前置通知：@Before 目标方法运行之前执行
			>
			>后置通知：@After 目标方法运行之后运行
			>
			>返回通知：@AfterRetuining 目标方法正常返回之后运行
			>
			>异常通知：@AfterThrowing在目标方法运行异常之后运行
			>
			>环绕通知:@Around 动态代理，手动推进目标方法运行(joinPoint.process())

4 给切面类的目标方法标注何时何地运行（通知注解）

```java
@Aspect
public class LogAspects {
    /**
     * 抽取公共切入点表达式
     * 1 本类引用:只要方法名
     * 2 其他切面引用:需要指定全类名
     */
    @Pointcut("execution(* com.able.springannocation.aop.*.*(..))")
    public void pointCut(){

    }
    @Before("pointCut()")
    public void logStart(){

    }
    @After("pointCut()")
    public void  logEnd(){

    }
    @AfterReturning("pointCut()")
    public void logReturn(){

    }
    @AfterThrowing("pointCut()")
    public void logException(){

    }
}
```



5 将切面类和业务逻辑类（目标方法所在类）都加入到容器中

```java
@Configuration
public class AOPConfig {

    @Bean
    public MathCalculator mathCalculator(){
        return new MathCalculator();
    }

    @Bean
    public LogAspects logAspects(){
        return new LogAspects();
    }
}
```



6 告诉Spring那个是切面类:

```java
@Aspect
public class LogAspects {
}
```

7 给配置类中加 @EnableAspectJAutoProxy 开启基于注解的aop模式

```java
//开启切面功能
@EnableAspectJAutoProxy
@Configuration
public class AOPConfig {

    @Bean
    public MathCalculator mathCalculator(){
        return new MathCalculator();
    }

    @Bean
    public LogAspects logAspects(){
        return new LogAspects();
    }
}
```



```java
@Aspect
@Slf4j
public class LogAspects {
    /**
     * 抽取公共切入点表达式
     * 1 本类引用:只要方法名
     * 2 其他切面引用:需要指定全类名
     */
    @Pointcut("execution(* com.able.springannocation.aop.*.*(..))")
    public void pointCut(){

    }
    @Before("pointCut()")
    public void logStart(JoinPoint joinPoint){
        String declaringTypeName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("logStart=================,类名为:{}方法名为:{},参数为:{}",declaringTypeName,methodName, Arrays.asList(args));

    }
    @After("pointCut()")
    public void  logAfter(JoinPoint joinPoint){
        log.info("logAfter=================");
    }
    @AfterReturning(value = "pointCut()",returning = "result")
    public void logReturn(Object result){
            log.info("接收到的返回值为:{}",result);
            log.info("logReturn=======================");
    }
    //JoinPoint 一定要出现在参数表的第一位
    @AfterThrowing(value = "pointCut()",throwing = "exception")
    public void logException(JoinPoint joinPoint,Exception exception){
        log.info("exception=",exception);
        log.info("logException===================================");
    }
}
```



三步：

​	将业务逻辑与切面类都加入到容器中：告诉Spring 哪个是切面类(@Aspect)

​	在切面类上的每一个通知方法上标注通知注解 告诉Spring何时何地运行（切入点表达式）

​	开启基于注解的apo模式

​					  

### AOP原理 【看给容器中注册了什么组件 这个组件什么时候工作 组件的功能是什么 】

@EnableAspectJAutoProxy：

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AspectJAutoProxyRegistrar.class)
public @interface EnableAspectJAutoProxy {

	/**
	 * Indicate whether subclass-based (CGLIB) proxies are to be created as opposed
	 * to standard Java interface-based proxies. The default is {@code false}.
	 */
	boolean proxyTargetClass() default false;

	/**
	 * Indicate that the proxy should be exposed by the AOP framework as a {@code ThreadLocal}
	 * for retrieval via the {@link org.springframework.aop.framework.AopContext} class.
	 * Off by default, i.e. no guarantees that {@code AopContext} access will work.
	 * @since 4.3.1
	 */
	boolean exposeProxy() default false;

}
```

​					

通过注解EnableAspectJAutoProxy 给容器中导入AspectJAutoProxyRegistrar这个组件

利用 AspectJAutoProxyRegistrar自定义给容器中注册bean 

​	org.springframework.aop.config.internalAutoProxyCreator :org.springframework.aop.aspectj.annotation.AnnotationAwareAspectJAutoProxyCreator

```java
@Nullable
	private static BeanDefinition registerOrEscalateApcAsRequired(
			Class<?> cls, BeanDefinitionRegistry registry, @Nullable Object source) {

		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");

		if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) {
			BeanDefinition apcDefinition = registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
			if (!cls.getName().equals(apcDefinition.getBeanClassName())) {
				int currentPriority = findPriorityForClass(apcDefinition.getBeanClassName());
				int requiredPriority = findPriorityForClass(cls);
				if (currentPriority < requiredPriority) {
					apcDefinition.setBeanClassName(cls.getName());
				}
			}
			return null;
		}

		RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
		beanDefinition.setSource(source);
		beanDefinition.getPropertyValues().add("order", Ordered.HIGHEST_PRECEDENCE);
		beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
		registry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition);
		return beanDefinition;
	}
```



```java
AnnotationAwareAspectJAutoProxyCreator
	->AspectJAwareAdvisorAutoProxyCreator
		->AbstractAdvisorAutoProxyCreator
			->AbstractAutoProxyCreator
				implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware
					关注后置处理器（在bean初始化完成前后做的事情） 自动注入beanFactory
		AbstractAutoProxyCreator.setBeanFactory
		AbstractAutoProxyCreator.有后置处理器的逻辑
		
		AbstractAdvisorAutoProxyCreator.setBeanFactory-->initBeanFactory

	
AnnotationAwareAspectJAutoProxyCreator.initBeanFactory
		
				
```

流程：

​	1 传入配置类 创建IOC容器		

​	2 注册配置类  调用refresh()刷新容器

​	3 registerBeanPostProcessors(beanFactory) 注册bean 的后置处理器来方便拦截bean的创建

​		1 先获取ioc容器中已经定义了的需要创建对象的所有BeanPostProcessor

​		2 给容器中添加别的BeanPostProcessor 

​		3 优先注册实现了 PriorityOrdered接口的BeanPostProcessor

​		4 再给容器中注册实现了Ordered接口的BeanPostProcessor

​		5  注册没有实现上面两个接口BeanPostProcesor

​		6 注册BeanPostProcessor 实际上就是创建BeanPostProcessor对象 保存在容器中

​			1 创建bean实例

​			2 populateBean(beanName, mbd, instanceWrapper)；给bean的属性赋值

​			3 initializeBean 初始化bean :

​					1.invokeAwareMethods:处理Aware接口的回调

​					2 applyBeanPostProcessorsBeforeInitialization：应用后置处理器的postProcessBeforeInitialization方法 

​					3invokeInitMethods :执行初始化方法

​					4 applyBeanPostProcessorsAfterInitialization():执行后置处理器的postProcessAfterInitialization方法

​		4  this.aspectJAdvisorFactory = new ReflectiveAspectJAdvisorFactory(beanFactory);

​		this.aspectJAdvisorsBuilder =      new BeanFactoryAspectJAdvisorsBuilderAdapter(beanFactory, this.aspectJAdvisorFactory);

​		7 把BeanPostProcessor 注册到BeanFactory中

​	4 finishBeanFactoryInitialization(beanFactory) 完成benaFactory初始化工作 完成剩下的单实例bean

​		1 遍历容器中所有的bean  依次创建对象：getBean(beanName)

​			getBean-->doGetBean-->getSingleton

​		2 创建Bean

​		AnnotationAwareAspectJAutoProxyCreator在所有bean创建之前会有一个拦截：InstantiationAwareBeanPostProcessor 

​			1 先从缓存中获取当前bean  如果能获取到 说明bean之前被创建过 直接使用 否则再创建 只要创建好的bean都会被缓存起来

​			2 createBean 创建bean

​			BeanPostProcessor是bean对象创建完成初始化前后调用的

​			InstantiationAwareBeanPostProcessor 是在创建bean实例之前先尝试用后置处理器返回对象的

​				1.resolveBeforeInstantiation //希望后置处理器在此能返回一个代理对象  如果能返回对象就使用 如果不能就继续。

​					bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);

​					bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);

​	Object beanInstance = doCreateBean(beanName, mbdToUse, args);真正去创建一个bean实例

AnnotationAwareAspectJAutoProxyCreator的【InstantiationAwareBeanPostProcessor 】作用

​	1 每个bean创建之前 调用postProcessBeforeInstantiation():

​		关心MathCalculator 和LogAspect的创建

​			1 判断当前bean是否在adviseBeans中(保存了所有需要增强bean)

​			2 判断当前bean是否是基础类型：

​				boolean retVal = Advice    Pointcut    Advisor      AopInfrastructureBean

​			或者是否是切面(Aspect)

​			3 是否需要跳过

​				获取候选的增强器（切面里的通知方法） List<Advisor> candidateAdvisors

​				I。每一个封装的通知方法的增强器为：InstantiationModelAwarePointcutAdvisor

​				判断每个增强器的类型是否为AspectJPointcutAdvisor 返回true

​				2 。永远返回false

​	创建对象

​		postProcessAfterInitialization

​			return wrapIfNecessary(bean, beanName, cacheKey);

​			1 获取当前bean的所有增强器 ：Object[] specificInterceptors

​				1 获取当前bean 的所有增强器（通知方法）

​				2 找到能在当前bean使用的增强器（找到那些通知方法是需要切入当前bean方法的）

​				3 给增强器排序

​			2 保存当前bean 在advisedBeans 中 

​			3.Object proxy = createProxy(      bean.getClass(), beanName, specificInterceptors, new SingletonTargetSource(bean)); 为当前bean的代理对象

​			1 获取所有增强器(通知方法)

​			2 保存到proxyFactory

​			3 创建代理对象

​					JdkDynamicAopProxy  jdk的动态代理

​					ObjenesisCglibAopProxy  cglib动态代理

​			4 给容器中返回当前组件使用cglib增强了的代理对象

​			5 以后容器获取到的就是这个组件的代理对象 代理对象就会执行通知方法的流程



```
* BeanPostProcessor:bean的后置处理器 bean 创建对象初始化前后进行拦截工作的
* BeanFactoryPostProcessor:beanFactory的后置处理器 ：
*            在beanFactory标准初始化之后调用
*            所有的bean定义已经保存加载到beanFactory中 但是bean的实例还未创建
*   流程:
*         IOC容器创建
*         执行invokeBeanFactoryPostProcessors
*         如何知道所有的BeanFactoryPostProcessor并执行他们的方法：
*         1.String[] postProcessorNames =
*           beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);
*        直接在beanFactory中找到所有的类型为 BeanFactoryPostProcessor的组件 并执行他们的方法
*         2 在初始创建其他组件前面执行
*BeanDefinitionRegistryPostProcessor:bean定义注册后置处理器
*  BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor
*  在所有的bean定义信息将要被加载 bean实例还没有创建 可以利用他给容器中额外添加组件
*      优先于BeanFactoryPostProcessor 的执行
*   流程 ：
*      IOC容器创建
*      refresh();方法调用
*      invokeBeanFactoryPostProcessors(beanFactory);的方法调用
*      postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
*      从容器中获取BeanDefinitionRegistryPostProcessor类型的所有组件
*      1 依次循环触发BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry的方法
*      2 在来循环触发BeanFactoryPostProcessor的postProcessBeanFactory方法
*
*      在从文档中找到 BeanFactoryPostProcessor
*      String[] postProcessorNames =
*           beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);
*        然后触发   postProcessBeanFactory方法
*
*
*
*   ApplicationListener:监听容器中法的时间。事件驱动模型开发
*          ApplicationListener<E extends ApplicationEvent> extends EventListener
*          监听ApplicationEvent及其下面的子事件
*     步骤：
*          写一个监听器来监（ApplicationListener的实现类）听某个事件（ApplicationEvent及其子类）
*          @EventListener
*          @EventListener(classes = {ApplicationEvent.class})
*          public void listener(ApplicationEvent applicationEvent){
*
*          }
*          原理：
*              EventListenerMethodProcessor
*              SmartInitializingSingleton原理：
*                  1 ioc容器创建对象并refresh()
*                  2 初始化所有剩下的单实例bean  finishBeanFactoryInitialization(beanFactory);
*                         1 先创建所有的单实例bean
*                         2 获取所有创建好的单实例bean 判断是否实现了 SmartInitializingSingleton这个接口
*                          如果是 则调用afterSingletonsInstantiated这个方法
*
*          把监听器加入到容器
*          只要容器中有相应的事件发布 我们就能监听到这个事件
*              1 ContextRefreshedEvent：容器刷新完成（所有bean都完全创建）会发布这个事件
*
*              2 自己发布事件
*              3 容器关闭会发布事件 ContextClosedEvent
    *
*
*         发布一个事件
*       原理: 1 容器创建对象：refresh();
*            2 容器刷新完成：finishRefresh();
*                  发布事件 容器刷新完成：publishEvent(new ContextRefreshedEvent(this));
*
*
*              事件发布流程：
*                  getApplicationEventMulticaster().multicastEvent(applicationEvent, eventType);
*                  获取事件的多播器(派发器)
*                  multicastEvent 派发事件
*                  getApplicationListeners(event, type)获取所有的指定事件监听器
*                  如果有executor 则可以进行异步派发 否则同步的方式直接执行
*                  invokeListener(listener, event) 拿到listener回调onApplicationEvent方法
*           applicationEventMulticaster【事件派发器】
*             initApplicationEventMulticaster();初始化applicationEventMulticaster
*             先去容器中寻找id为applicationEventMulticaster的组件
*             如果没有 则创建并添加到容器中取
*              this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
*        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
*     容器中有哪些监听器
*     1 容器创建对象:refresh();
*     2 registerListeners();
*     从容器中拿到所有的监听器 吧他们添加到ApplicationEventMulticaster中去
*     String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
*     for (String listenerBeanName : listenerBeanNames) {
*        getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
*     }
```

​			

### Spring容器的refresh() 创建刷新

​	1 prepareRefresh();//Prepare this context for refreshing. 容器刷新前的预处理

​		1 initPropertySources(); 初始化属性设置 子类自定义个性化的属性设置方法

​		 2 getEnvironment().validateRequiredProperties() 属性的合法性检测

​		3 this.earlyApplicationListeners = new LinkedHashSet<>(this.applicationListeners); 保存容器的一些早期的事件

​      2 ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory(); 获取刷新bean工厂

​			1 refreshBeanFactory();

​					GenericApplicationContext 构造函数中  this.beanFactory = new DefaultListableBeanFactory();

​					创建 beanFactory 并设置id 

​			2 getBeanFactory() 获取BeanFactory  

​			3 将创建的beanFactory【DefaultListableBeanFactory】返回 

​	3 prepareBeanFactory(beanFactory); BeanFactory的预处理工作（BeanFactory进行一些设置）

​		1 设置BeanFactory的类加载器 支持表达式解析器。。。

​		2 添加部分BeanPostProcessor(ApplicationContextAwareProcessor)

​		3 设置回来的自动装配的接口EnvironmentAware EmbeddedValueResolverAware。。。

​		4 注册可以解析的自动装配

​		5 添加后置处理器 BeanPostProcessor（ApplicationListenerDetector）

​		6 添加编译时的AspectJ

​		7 给BeanFactory中添加 environment【ConfigurableEnvironment】 

​			systemProperties 【Map<String, Object>】

​			systemEnvironment【Map<String, Object>】等组件

​	4 postProcessBeanFactory(beanFactory);（一个空方法） beanFactory准备工作完成后的后置处理工作

​		1 子类可以通过重写这个方法 在beanFactory创建并预准备完成后 做进一步设置

​	==================beanFactory创建以及预准备工作=================================

​	5 invokeBeanFactoryPostProcessors(beanFactory)执行beanFactory的postProcess

​			在BeanFactory标准初始化之后执行的

​			两个接口:BeanFactoryPostProcessor   BeanDefinitionRegistryPostProcessor

​			1 PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors()); 执行postProcessBeanDefinitionRegistry

​			   		1.String[] postProcessorNames =      beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false); 获取所有的BeanDefinitionRegistryPostProcessor的名称

​					2 看优先级排序 PriorityOrdered  

​						执行 invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);

​					 3 再执行实现了Ordered 顺序接口的 BeanDefinitionRegistryPostProcessor

​						执行 invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);

​					4  最后执行没有实现任何接口的BeanDefinitionRegistryPostProcessor

​						执行  invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);

​					5 执行BeanDefinitionRegistryPostProcessor的PostProcessor方法

​							invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);

​		                                         invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);	



​				2String[] postProcessorNames =      beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);获取所有的 BeanFactoryPostProcessor的方法

​			1 获取所有的BeanFactoryPostProcessor

​			2 先执行实现了PriorityOrdered优先级接口的BeanFactoryPostProcessor

​				postProcessor.postProcessBeanFactory

​			2 再执行实现了Ordered接口的BeanFactoryPostProcessor

​				postProcessor.postProcessBeanFactory

​			3 最后执行没有实现PriorityOrdered 或者Ordered接口的BeanFactoryPostProcessor

​				postProcessor.postProcessBeanFactory

​	6  registerBeanPostProcessors(beanFactory); 注册BeanPostProcessor【bean的后置处理器】

​	//// Register bean processors that intercept bean creation. 用于拦截bean的创建

​		  

​		不同类型的BeanPostProcessor   在Bean创建的前后的执行时机不同

​		BeanPostProcessor 

​		DestructionAwareBeanPostProcessor   

​		SmartInstantiationAwareBeanPostProcessor	

​		InstantiationAwareBeanPostProcessor

​		MergedBeanDefinitionPostProcessor

1.String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);获取所有的BeanPostProcessor 

​	1 获取所有的BeanPostProcessor 货值处理器默认可以通过 PriorityOrdered Ordered接口等分到不同的list中 

​	1 对实现了PriorityOrdered 接口的BeanPostProcessor进行排序然后注册（所谓注册 就是将beanPostProcessor添加到 beanFactory中）

​		private static void registerBeanPostProcessors(      ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {   for (BeanPostProcessor postProcessor : postProcessors) {      beanFactory.addBeanPostProcessor(postProcessor);   }}

​	2 对实现了Ordered 接口的BeanPostProcessor进行排序然后注册

​	3 对没有实现任何接口的BeanPostProcessor进行排序 然后注册

​	4 最终注册实现了MergedBeanDefinitionPostProcessor接口的BeanPostProcessor  

​	5 最后注册一个  ApplicationListenerDetector(applicationContext)来在Bean创建完成后检查是否是ApplicationListener 如果是 则添加到容器中去



7 initMessageSource(); 初始化MessageSource组件（做国际化功能 消息绑定 消息解析）

​	1 获取beanFactory 

​	2 判断容器中是否存在messageSource的这个bean 有则赋值给messageSources 

​	没有则创建一个默认的new DelegatingMessageSource()给messageSources 属性

​		MessageSource 取出国际化配置文件中的某个key的值 按照区域信息获取

​	3 把创建好的messageSources注册到容器中 以后获取国际化配置文件值的时候 可以自动注入

​		beanFactory.registerSingleton(MESSAGE_SOURCE_BEAN_NAME, this.messageSource);

8  initApplicationEventMulticaster(); 初始化事件派发器

​		1 获取beanFactory

​		2 判断容器中是否存在 applicationEventMulticaster 存在则将其赋值给属性applicationEventMulticaster

​		不存在则创建默认的派发器 new SimpleApplicationEventMulticaster(beanFactory) 将其赋值给applicationEventMulticaster  并将其添加到容器中去



9 onRefresh(); 留给子类重写	

​			1 在容器刷新的时候自定义逻辑



10 registerListeners(); 给容器中注册监听器

​		1 String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);

​		2 从容器中获取所有的ApplicationListener的名字 将其添加到事件派发器中

​		getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);

​		3 派发之前步骤产生的事件

11 finishBeanFactoryInitialization(beanFactory); 初始化所有剩下单例的bean



​		







​				

​			



​		