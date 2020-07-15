package com.peigong.springcloudalibaba.impl;

import com.peigong.springcloudalibaba.HelloService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author: lilei
 * @create: 2020-07-15 14:56
 **/
@Service
public class HelloServiceImpl implements HelloService {

    @Value("${spring.application.name}")
    private String serviceName;

    public String hello(String name) {
        return String.format("[%s]: Hello, %s",serviceName,name);
    }
}
