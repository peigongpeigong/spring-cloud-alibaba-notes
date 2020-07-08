package com.peigong.springcloudalibaba.user;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.io.IOException;

/**
 * @author: lilei
 * @create: 2020-07-08 16:29
 **/
public class DubboMain {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("classpath*:/META-INF/spring/user-provider.xml");
        ctx.start();
        System.in.read();
    }

}
