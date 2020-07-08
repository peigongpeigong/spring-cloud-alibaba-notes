package com.peigong.springcloudalibaba;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: lilei
 * @create: 2020-07-08 15:15
 **/
@ConfigurationProperties(prefix="pg.redis")
public class RedissonProperties {

    private String host = "localhost";
    private int port = 6379;
    private boolean ssl;
    private String password;
    private int timeout;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isSsl() {
        return ssl;
    }

    public void setSsl(boolean ssl) {
        this.ssl = ssl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
