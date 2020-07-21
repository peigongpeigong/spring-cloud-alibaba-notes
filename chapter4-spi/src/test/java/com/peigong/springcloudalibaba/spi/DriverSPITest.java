package com.peigong.springcloudalibaba.spi;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.junit.Test;

/**
 * @author: lilei
 * @create: 2020-07-16 15:05
 **/
public class DriverSPITest {

    @Test
    public void testDriver() {
        ExtensionLoader<Driver> extensionLoader = ExtensionLoader.getExtensionLoader(Driver.class);
        //Driver driver = extensionLoader.getExtension("mysqlDriver");
        Driver driver = extensionLoader.getDefaultExtension();
        System.out.println(driver.connect());
    }

}
