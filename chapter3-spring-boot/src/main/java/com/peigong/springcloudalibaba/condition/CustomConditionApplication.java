package com.peigong.springcloudalibaba.condition;

import com.peigong.springcloudalibaba.selector.AnotherBizClass;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: lilei
 * @create: 2020-07-08 15:10
 **/
public class CustomConditionApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(ConditionConfig.class);
        AnotherBizClass bean = ctx.getBean(AnotherBizClass.class);
        System.out.println(bean);
    }

}
