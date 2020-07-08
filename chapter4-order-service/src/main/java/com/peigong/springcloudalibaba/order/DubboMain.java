package com.peigong.springcloudalibaba.order;

import com.peigong.springcloudalibaba.user.UserService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author: lilei
 * @create: 2020-07-08 17:26
 **/
public class DubboMain {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:/META-INF/spring/consumer.xml");
        context.start();
        UserService userService = (UserService) context.getBean("userService");
        System.out.println(userService.getNameById("1001"));
    }

}
