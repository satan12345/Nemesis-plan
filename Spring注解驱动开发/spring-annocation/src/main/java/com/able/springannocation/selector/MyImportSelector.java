package com.able.springannocation.selector;

import com.able.springannocation.bean.Bule;
import com.able.springannocation.bean.Green;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author jipeng
 * @date 2019-03-01 16:06
 * @description
 */
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

