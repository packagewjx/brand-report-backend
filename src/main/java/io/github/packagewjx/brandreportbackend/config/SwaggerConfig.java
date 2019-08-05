package io.github.packagewjx.brandreportbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:wu812730157@gmail.com">Junxian Wu</a>
 * @date 19-8-3
 **/
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket api() {
        ResponseMessage badRequestMessage = new ResponseMessageBuilder().code(400).message("请求有误").build();
        ResponseMessage internalServerErrorMessage = new ResponseMessageBuilder().code(500).message("服务器内部错误").build();
        ResponseMessage forbiddenMessage = new ResponseMessageBuilder().code(403).message("没有权限访问本接口").build();
        List<ResponseMessage> defaultMessages = Arrays.asList(badRequestMessage, internalServerErrorMessage, forbiddenMessage);

        List<ResponseMessage> getResponseMessages = new ArrayList<>();
        getResponseMessages.add(new ResponseMessageBuilder().code(200).message("获取成功").build());
        getResponseMessages.add(new ResponseMessageBuilder().code(404).message("没有找到对应的实体").build());
        getResponseMessages.addAll(defaultMessages);

        List<ResponseMessage> putResponseMessages = new ArrayList<>();
        putResponseMessages.add(new ResponseMessageBuilder().code(200).message("更新成功").build());
        putResponseMessages.addAll(defaultMessages);

        List<ResponseMessage> postResponseMessages = new ArrayList<>();
        postResponseMessages.add(new ResponseMessageBuilder().code(201).message("插入成功").build());
        postResponseMessages.addAll(defaultMessages);

        List<ResponseMessage> deleteResponseMessage = new ArrayList<>();
        deleteResponseMessage.add(new ResponseMessageBuilder().code(200).message("删除成功").build());
        deleteResponseMessage.addAll(defaultMessages);

        ApiInfo apiInfo = new ApiInfoBuilder()
                .title("品牌分析报告系统")
                .description("品牌分析报告系统接口")
                .version("1.0.0")
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.github.packagewjx.brandreportbackend.controller"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo)
                .useDefaultResponseMessages(false)
                .globalResponseMessage(RequestMethod.GET, getResponseMessages)
                .globalResponseMessage(RequestMethod.PUT, putResponseMessages)
                .globalResponseMessage(RequestMethod.POST, postResponseMessages)
                .globalResponseMessage(RequestMethod.DELETE, deleteResponseMessage)
                .enableUrlTemplating(true);
    }
}
