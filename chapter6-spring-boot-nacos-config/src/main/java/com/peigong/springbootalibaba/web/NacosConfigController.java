package com.peigong.springbootalibaba.web;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.peigong.springbootalibaba.component.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: lilei
 * @create: 2020-07-22 15:15
 **/
@RestController
public class NacosConfigController {

    @Autowired
    private Configuration conf;

    @GetMapping("info")
    public String info(){
        return conf.getInfo();
    }

}
