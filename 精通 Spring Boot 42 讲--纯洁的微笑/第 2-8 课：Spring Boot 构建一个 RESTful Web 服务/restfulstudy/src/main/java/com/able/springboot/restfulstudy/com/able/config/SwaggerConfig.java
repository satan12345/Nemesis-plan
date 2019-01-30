package com.able.springboot.restfulstudy.com.able.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author jipeng
 * @date 2019-01-30 14:26
 * @description
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket docket(){
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.able.springboot.restfulstudy.com.able.controller"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("客户管理")
                .description("客户管理中心 API 1.0操作文档")
                .termsOfServiceUrl("www.baidu.com")
                .version("1.0.0")
                .contact(new Contact("satan","www.baidu.com","satan@163.com"))
                .build();
    }
}

