package com.peigong.springcloudalibaba.service;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: lilei
 * @create: 2020-07-25 15:40
 **/
@Component
public class JsonResponseBLockHandler implements UrlBlockHandler {
    @Override
    public void blocked(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws IOException {
        httpServletResponse.setHeader("Content-Type", "application/json;charset=utf-8");
        //language=JSON
        String msg = "{\"code\":999,\"msg\": \"访问人数过多，请稍后再试\"}";
        httpServletResponse.getWriter().write(msg);
    }
}
