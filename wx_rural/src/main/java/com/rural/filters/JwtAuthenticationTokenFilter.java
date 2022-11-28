package com.rural.filters;

import com.rural.pojo.AdminUserDetails;
import com.rural.pojo.WeChatUserDetails;
import com.rural.utils.JwtUtil;
import com.rural.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * @author
 * @create 2022-10-14  16:30
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    private RedisCache redisCache;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token;
        boolean f = false; // false:wx_token不为空，true:ht_token不为空
        // 获取 token
        String wxToken = request.getHeader("wx_token");
        String htToken = request.getHeader("ht_token");
        // 判断 token 如果都为空
        if(!StringUtils.hasText(wxToken) && !StringUtils.hasText(htToken)){
            // 放行
            filterChain.doFilter(request,response);
            return;
        }
        else if(StringUtils.hasText(wxToken)){
            f = false;
            token = wxToken;
        }else{
            f = true;
            token = htToken;
        }
        // 解析 token , 获取 id 或者 openId
        String id;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            id = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("token非法");
        }
        // 通过 Redis 获取 LoginUser
        AdminUserDetails adminUserDetails = null;
        WeChatUserDetails weChatUserDetails = null;
        if(f){
            adminUserDetails = redisCache.getCacheObject("login_ht:" + id);
        }else{
            weChatUserDetails = redisCache.getCacheObject("login_wx:" + id);
        }

        // 判断 UserDetails 是否为空
        if(Objects.isNull(f?adminUserDetails:weChatUserDetails)){
            throw new RuntimeException("用户未登录");
        }
        // 把认证后的信息存储到SecurityContextHolder中
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(f?adminUserDetails:weChatUserDetails,null,f?adminUserDetails.getAuthorities():weChatUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        // 放行
        filterChain.doFilter(request,response);
    }
}
