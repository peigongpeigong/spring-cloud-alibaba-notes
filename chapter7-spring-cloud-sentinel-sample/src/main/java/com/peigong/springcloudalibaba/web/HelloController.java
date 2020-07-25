package com.peigong.springcloudalibaba.web;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lilei
 * @create: 2020-07-25 14:47
 **/
@RestController
public class HelloController {

    @SentinelResource(value = "hello", blockHandler = "helloBlockHandler")
    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    public String helloBlockHandler(BlockException e) {
        return "限流了";
    }

}
