package com.able.springbootes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springboot 默认支持两种技术来与es交互
 * 1 jest(默认不生效)
 *  需要导入jest工具包io.searchbox.client.JestClient;
 *  2 springData ElasticSearch
 *      1. Client 节点信息ClusrerNode;clusterName
 *      2 ElastiosearchTemplate操作es
 *      3 编写一个ElasticsearchRepository的子接口来操作es
 */
@SpringBootApplication
public class SpringbootEsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootEsApplication.class, args);
    }

}

