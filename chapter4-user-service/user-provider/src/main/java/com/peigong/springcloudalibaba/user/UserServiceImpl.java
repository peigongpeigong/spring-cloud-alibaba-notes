package com.peigong.springcloudalibaba.user;

/**
 * @author: lilei
 * @create: 2020-07-08 16:13
 **/
public class UserServiceImpl implements UserService{
    @Override
    public String getNameById(String uid) {
        System.out.println("uid from remote:" + uid);
        //TODO 省略DB操作
        return "MockName";
    }
}
