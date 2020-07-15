package com.peigong.springbootalibaba;

import com.peigong.springbloudalibaba.HelloService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author: lilei
 * @create: 2020-07-09 11:36
 **/
@SpringBootApplication
public class ConsumerApplication {

    @DubboReference(url = "dubbo://192.168.2.99:20880/com.peigong.springbloudalibaba.HelloService")
    private HelloService helloService;

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner(){
        return args -> System.out.println(helloService.hello("peigong"));
    }

}
