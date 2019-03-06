package com.able.springannocation.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author jipeng
 * @date 2019-03-01 15:34
 * @description
 */
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

