package com.peigong.springcloudalibaba.spi;

import org.apache.dubbo.common.extension.SPI;

@SPI(value = "mysqlDriver")
public interface Driver {

    String connect();

}
