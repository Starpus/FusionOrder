package com.fusionorder.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger/OpenAPI 配置类
 * 配置交互式API文档，支持在线测试接口
 * 
 * @author FusionOrder Team
 */
@Configuration
public class SwaggerConfig {
    
    /**
     * 配置OpenAPI基本信息
     * 包括API文档标题、版本、描述等信息
     * 
     * @return OpenAPI配置对象
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FusionOrder API 文档")
                        .version("1.0.0")
                        .description("FusionOrder 订单管理系统 API 接口文档\n\n" +
                                "提供用户管理、产品管理、订单管理等功能的RESTful API接口\n\n" +
                                "**认证说明**: 部分接口需要JWT Token认证，请在登录接口获取Token后，" +
                                "在右上角点击'Authorize'按钮，输入 'Bearer {token}' 进行认证")
                        .contact(new Contact()
                                .name("FusionOrder Team")
                                .email("support@fusionorder.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Token认证，格式: Bearer {token}")));
    }
}

