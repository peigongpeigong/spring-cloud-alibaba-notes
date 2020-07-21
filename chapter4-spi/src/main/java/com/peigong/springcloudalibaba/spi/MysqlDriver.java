package com.peigong.springcloudalibaba.spi;

/**
 * @author: lilei
 * @create: 2020-07-16 15:05
 **/
public class MysqlDriver implements Driver{
    public String connect() {
        return "mysql driver";
    }
}
