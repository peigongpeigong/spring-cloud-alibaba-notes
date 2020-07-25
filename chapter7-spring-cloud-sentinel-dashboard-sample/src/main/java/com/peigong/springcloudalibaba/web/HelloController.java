package com.peigong.springcloudalibaba.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lilei
 * @create: 2020-07-25 15:05
 **/
@RestController
public class HelloController {

    @GetMapping("hello")
    public String hello(){
        return "hello";
    }

}
