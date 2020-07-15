package com.peigong.springcloudalibaba;

import com.peigong.springbloudalibaba.HelloService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author: lilei
 * @create: 2020-07-15 14:04
 **/
@SpringBootApplication
public class ConsumerApplication {

    @DubboReference()
    private HelloService helloService;

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner(){
        return args -> System.out.println(helloService.hello("peigong"));
    }

}
