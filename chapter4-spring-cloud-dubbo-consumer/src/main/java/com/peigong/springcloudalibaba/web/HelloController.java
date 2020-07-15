package com.peigong.springcloudalibaba.web;

import com.peigong.springcloudalibaba.HelloService;
import com.peigong.springcloudalibaba.service.mock.MockHelloService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lilei
 * @create: 2020-07-15 15:13
 **/
@RestController
public class HelloController {

    @Reference(mock= "com.peigong.springcloudalibaba.service.mock.MockHelloService",cluster = "failfast")
    private HelloService helloService;

    @GetMapping("hello")
    public Object hello(){
        return helloService.hello("peigong");
    }

}
