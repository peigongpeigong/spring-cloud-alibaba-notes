package com.peigong.springcloudalibaba;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @author: lilei
 * @create: 2020-07-08 15:19
 **/
@Configuration
@ConditionalOnClass(Redisson.class)
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {

    @Bean
    RedissonClient redissonClient(RedissonProperties properties) {
        Config config = new Config();
        SingleServerConfig sc = config.useSingleServer();
        String prefix;
        if (properties.isSsl()) {
            prefix = "rediss://";
        }else{
            prefix = "redis://";
        }
        sc.setAddress(prefix + properties.getHost() + ":" + properties.getPort());
        if (StringUtils.isEmpty(properties.getPassword())) {
            sc.setPassword(properties.getPassword());
        }
        if (properties.getTimeout() > 0) {
            sc.setTimeout(properties.getTimeout());
        }
        return Redisson.create(config);
    }

}
