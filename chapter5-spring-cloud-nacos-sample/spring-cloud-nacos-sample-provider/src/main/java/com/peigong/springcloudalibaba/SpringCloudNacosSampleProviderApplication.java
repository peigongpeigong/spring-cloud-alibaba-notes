package com.peigong.springcloudalibaba;

import org.apache.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: lilei
 * @create: 2020-07-21 14:50
 **/
@SpringBootApplication
//@DubboComponentScan
public class SpringCloudNacosSampleProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudNacosSampleProviderApplication.class, args);
    }

}
