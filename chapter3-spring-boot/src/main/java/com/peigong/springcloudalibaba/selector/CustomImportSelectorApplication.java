package com.peigong.springcloudalibaba.selector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author: lilei
 * @create: 2020-07-08 15:05
 **/
@SpringBootApplication
@EnableAutoImport
public class CustomImportSelectorApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(CustomImportSelectorApplication.class);
        BizClass bean = ctx.getBean(BizClass.class);
        System.out.println(bean);
    }

}
