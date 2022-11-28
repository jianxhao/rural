package com.rural.handler;

import com.alibaba.fastjson.JSON;
import com.rural.pojo.ResponseResult;
import com.rural.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author
 * @create 2022-10-17  16:34
 */
@Component
public class AuthenticationEntryOPointImpl implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        ResponseResult result = new ResponseResult(HttpStatus.UNAUTHORIZED.value(),"认证未通过，请检查登录状况");
        String json = JSON.toJSONString(result);
        WebUtils.renderString(response,json);
    }
}
