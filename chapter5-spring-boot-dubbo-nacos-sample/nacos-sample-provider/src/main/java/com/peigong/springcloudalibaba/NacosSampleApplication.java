package com.peigong.springcloudalibaba;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: lilei
 * @create: 2020-07-21 11:43
 **/
@SpringBootApplication
@DubboComponentScan
public class NacosSampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(NacosSampleApplication.class, args);
    }

}
