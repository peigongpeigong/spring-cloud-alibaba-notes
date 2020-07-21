package com.peigong.springcloudalibaba.service;

import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author: lilei
 * @create: 2020-07-21 11:38
 **/
@DubboService
public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String name) {
        return "Hello " + name;
    }
}
