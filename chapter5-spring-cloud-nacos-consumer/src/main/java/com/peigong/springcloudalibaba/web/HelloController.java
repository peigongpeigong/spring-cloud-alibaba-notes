package com.peigong.springcloudalibaba.web;

import com.peigong.springcloudalibaba.service.HelloService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lilei
 * @create: 2020-07-21 15:04
 **/
@RestController
public class HelloController {

    @Reference
    private HelloService helloService;

    @GetMapping("hello")
    public Object hello(String name) {
        return helloService.hello(name);
    }

}
