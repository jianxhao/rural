package com.rural.utils;

import com.rural.pojo.AdminUserDetails;
import com.rural.pojo.WeChatUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author
 * @create 2022-10-26  10:37
 */
@Component
public class SecurityContextHolderUtil {
    public static Integer getUserIdInAdminUserDetails(){
        // 通过SecurityContextHolder 获取 authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 得到 AdminUserDetails
        AdminUserDetails adminUserDetails = (AdminUserDetails) authentication.getPrincipal();
        // 获取id
        Integer userId = adminUserDetails.getAdminUser().getId();
        return userId;
    }
    public static String getUserIdInWeChatUserDetails(){
        // 通过SecurityContextHolder 获取 authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // 得到 WeChatUserDetails
        WeChatUserDetails weChatUserDetails = (WeChatUserDetails) authentication.getPrincipal();
        // 获取openid
        String openId = weChatUserDetails.getWxUser().getOpenId();
        System.out.println(openId);
        return openId;
    }
}
