package com.peigong.springbootalibaba.component;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.springframework.stereotype.Component;

/**
 * @author: lilei
 * @create: 2020-07-22 16:30
 **/
@Component
@NacosPropertySource(dataId = "example",autoRefreshed = true)
public class Configuration {

    @NacosValue(value = "${info:Default Props}",autoRefreshed = true)
    private String info;

    public String getInfo() {
        return info;
    }
}
