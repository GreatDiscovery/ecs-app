package com.gavin.app;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author gavin
 * @date 2020/2/22 10:11 上午
 */
@Configuration
@EnableSwagger2
@EnableWebMvc
public class Swagger2 {

//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .apiInfo(apiInfo())
//                .select()
//                // 为当前包路径
//                .apis(RequestHandlerSelectors.basePackage("com.gavin.app"))
//                .paths(PathSelectors.any())
//                .build();
//    }
//
//    /**
//     * 构建 api 文档的详细信息函数，注意这里的注解引用的是哪个
//     */
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                // 页面标题
//                .title("SSM Application [ecs] Swagger2 RESTFul API")
//                // 创建人
//                .contact(new Contact("gavin", "http://www.x-sir.com", "1303535630@qq.com"))
//                // 版本号
//                .version("1.0")
//                // 描述
//                .description("This is [ecs] API doc")
//                .build();
//    }

    @Bean
    public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            //为当前包下controller生成API文档
            .apis(RequestHandlerSelectors.basePackage("com.gavin.app"))
            //为有@Api注解的Controller生成API文档
//                .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
            //为有@ApiOperation注解的方法生成API文档
//                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
            .paths(PathSelectors.any())
            .build();
}

    private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
            .title("SwaggerUI演示")
            .description("mall-tiny")
            .contact("macro")
            .version("1.0")
            .build();
}
}

