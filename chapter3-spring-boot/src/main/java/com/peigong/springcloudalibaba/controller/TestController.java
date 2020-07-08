package com.peigong.springcloudalibaba.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: lilei
 * @create: 2020-07-08 14:48
 **/
@RestController
public class TestController {

    @RequestMapping(value = "test")
    public Object test() {
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        return map;
    }

}
