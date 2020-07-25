package com.peigong.springcloualibaba.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lilei
 * @create: 2020-07-25 16:38
 **/
@RestController
public class HelloController {

    @GetMapping("hello")
    public String hello(){
        return "hello";
    }

}
