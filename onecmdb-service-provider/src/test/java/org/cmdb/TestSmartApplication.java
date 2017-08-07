package org.cmdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by X on 2017/3/30.
 * Smart应用测试启动入口
 */

@ComponentScan
@SpringBootApplication
@EnableAutoConfiguration
public class TestSmartApplication {
    public static void main(String[] args) {
        SpringApplication.run(TestSmartApplication.class, args);
    }
}
