package com.peigong.springcloudalibaba.condition;

import com.peigong.springcloudalibaba.selector.AnotherBizClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author: lilei
 * @create: 2020-07-08 15:08
 **/
@Configuration
public class ConditionConfig {

    @Bean
    @Conditional(CustomCondition.class)
    public AnotherBizClass anotherBizClass(){
        return new AnotherBizClass();
    }

}
