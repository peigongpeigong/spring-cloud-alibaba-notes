package com.peigong.springcloudalibaba;

import com.alibaba.csp.sentinel.annotation.aspectj.SentinelResourceAspect;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author: lilei
 * @create: 2020-07-24 15:15
 **/
@SpringBootApplication
public class SentinelApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(SentinelApplication.class, args);
        SentinelSampleWithAnnotation.initFlowRules();
        SentinelSampleWithAnnotation sample = ctx.getBean(SentinelSampleWithAnnotation.class);
        while (true) {
            try {
                sample.hello();
            } catch (Exception e) {
                //e.printStackTrace();
            }
        }
    }


}
