package com.fusionorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * FusionOrder 应用主类
 * SpringBoot 应用程序的入口点
 * 
 * @author FusionOrder Team
 */
@SpringBootApplication
public class FusionOrderApplication {
    
    /**
     * 应用程序入口方法
     * 
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(FusionOrderApplication.class, args);
    }
}

