package com.rural.controller;

import com.rural.pojo.AdminUser;
import com.rural.pojo.ResponseResult;
import com.rural.pojo.WxInfo;
import com.rural.service.AdminUserService;
import com.rural.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xh
 * @create 2022-11-18  9:34
 */
@RestController
public class UserLoginOrLogout {
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private WxUserService wxUserService;
    // 后台登录
    @PostMapping("/admin/login")
    public ResponseResult adminLogin(@RequestBody AdminUser adminUser){
        return adminUserService.adminLogin(adminUser);
    }
    // 客户端登录
    @PostMapping("/wx/login")
    public ResponseResult WxLogin(@RequestBody WxInfo wxInfo){
        try {
            ResponseResult wxUser = wxUserService.getWxUserInfo(wxInfo);
            return wxUser;
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(201,"验证失败");
        }
    }
    // 后台退出
    @GetMapping("/admin/logout")
    public ResponseResult adminLogout(){
        return adminUserService.logout();
    }
    // 微信退出
    @GetMapping("/wx/logout")
    public ResponseResult wxLogout(){
        return wxUserService.logout();
    }

}
