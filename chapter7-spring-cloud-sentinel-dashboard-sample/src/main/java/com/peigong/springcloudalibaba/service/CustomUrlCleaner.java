package com.peigong.springcloudalibaba.service;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author: lilei
 * @create: 2020-07-25 16:25
 **/
@Component
public class CustomUrlCleaner implements UrlCleaner {
    @Override
    public String clean(String s) {
        if (StringUtils.isEmpty(s)) {
            return s;
        }
        if (s.startsWith("/clean/")) {
            return "/clean/*";
        }
        return s;
    }
}
