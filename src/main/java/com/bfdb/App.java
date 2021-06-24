package com.bfdb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 项目运行入口
 *
 */

@EnableAutoConfiguration
@ComponentScan(basePackages = "com.bfdb") //配置注解扫描的包
@EnableTransactionManagement//开启事务
@MapperScan("com.bfdb.mapper")
@SpringBootApplication
public class App {

    public static void main( String[] args ){
        System.setProperty("java.net.preferIPv4Stack", "true");
        SpringApplication.run(App.class,args);//运行项目 也可以单独在controller上这样运行

    }

}
