package com.peigong.springcloudalibaba;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author: lilei
 * @create: 2020-07-22 15:46
 **/
@SpringBootApplication
public class NacosConfigApplication {

    public static void main(String[] args) throws InterruptedException{
        ConfigurableApplicationContext context = SpringApplication.run(NacosConfigApplication.class, args);
        while (true) {
            String info = context.getEnvironment().getProperty("info");
            System.out.println(info);
            Thread.sleep(2000);
        }
    }

}
