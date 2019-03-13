package com.able.springannocation.config;

import com.able.springannocation.bean.Red;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author jipeng
 * @date 2019-03-08 14:02
 * @description
 * BeanPostProcessor:bean的后置处理器 bean 创建对象初始化前后进行拦截工作的
 * BeanFactoryPostProcessor:beanFactory的后置处理器 ：
 *            在beanFactory标准初始化之后调用
 *            所有的bean定义已经保存加载到beanFactory中 但是bean的实例还未创建
 *   流程:
 *         IOC容器创建
 *         执行invokeBeanFactoryPostProcessors
 *         如何知道所有的BeanFactoryPostProcessor并执行他们的方法：
 *         1.String[] postProcessorNames =
 * 				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);
 * 			直接在beanFactory中找到所有的类型为 BeanFactoryPostProcessor的组件 并执行他们的方法
 * 		    2 在初始创建其他组件前面执行
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
 * 				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);
 * 			然后触发	postProcessBeanFactory方法
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
 *             	this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
 * 			beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
 * 		容器中有哪些监听器
 * 	   1 容器创建对象:refresh();
 * 	   2 registerListeners();
 * 	   从容器中拿到所有的监听器 吧他们添加到ApplicationEventMulticaster中去
 * 	   String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
 * 		for (String listenerBeanName : listenerBeanNames) {
 * 			getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
 * 		}
 *
 *
 *
 */
@ComponentScan(basePackages = {"com.able.springannocation.ext"})
public class ExtConfig {

    @Bean
    public Red red(){
        return new Red();
    }

}

