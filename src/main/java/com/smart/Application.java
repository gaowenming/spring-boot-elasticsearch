package com.smart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author gaowenming
 * @create 2017-12-16 21:03
 **/
@SpringBootApplication
@ComponentScan(value = {"com.smart.elasticsearch"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
