package com.peigong.springcloudalibaba.impl;

import com.peigong.springbloudalibaba.HelloService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Value;

/**
 * @author: lilei
 * @create: 2020-07-09 11:19
 **/
@DubboService
public class HelloServiceImpl implements HelloService {

    @Value("${dubbo.application.name}")
    private String serviceName;

    @Override
    public String hello(String name) {
        return String.format("[%s]: Hello %s",serviceName,name);
    }
}
