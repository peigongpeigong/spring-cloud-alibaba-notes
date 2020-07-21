package com.peigong.springcloudalibaba.service;

import org.apache.dubbo.config.annotation.Service;

/**
 * @author: lilei
 * @create: 2020-07-21 12:03
 **/
@Service
public class HelloServiceImpl implements HelloService{
    @Override
    public String hello(String name) {
        return "Hello " + name;
    }

}
