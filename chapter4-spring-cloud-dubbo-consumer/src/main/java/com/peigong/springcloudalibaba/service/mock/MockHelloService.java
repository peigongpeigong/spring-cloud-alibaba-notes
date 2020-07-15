package com.peigong.springcloudalibaba.service.mock;

import com.peigong.springcloudalibaba.HelloService;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

/**
 * @author: lilei
 * @create: 2020-07-15 16:49
 **/
@Service
@Component
public class MockHelloService implements HelloService {
    @Override
    public String hello(String name) {
        return "降级后数据";
    }
}
