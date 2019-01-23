package com.able.esstudy.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;

/**
 * @author jipeng
 * @date 2019-01-22 20:47
 * @description
 */
@Configuration
public class EsConfig {
    @Bean
    public TransportClient client() throws Exception {

        //设置集群名称
        Settings settings = Settings.builder().put("cluster.name", "my-application").build();
        //创建client
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.119.158"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("192.168.119.159"), 9300));

        return client;

    }
}

