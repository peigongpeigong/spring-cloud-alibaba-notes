package com.peigong.springcloudalibaba.web;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lilei
 * @create: 2020-07-17 16:07
 **/
@RestController
public class NacosTestController {

    @NacosInjected
    private NamingService namingService;

    @GetMapping("discovery")
    public Object discovery(String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }


}
