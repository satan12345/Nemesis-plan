package com.able.springannocation.filter;

import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;

/**
 * @author jipeng
 * @date 2019-03-01 13:22
 * @description
 */
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

