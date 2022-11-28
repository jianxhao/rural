package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rural.mapper.AdminUserMapper;
import com.rural.mapper.RoleMapper;
import com.rural.mapper.WxUserMapper;
import com.rural.pojo.AdminUser;
import com.rural.pojo.AdminUserDetails;
import com.rural.pojo.WxUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author xh
 * @create 2022-11-18  10:03
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AdminUserMapper adminUserMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查找admin用户表是否为空
        AdminUser adminUser = adminUserMapper.selectOne(new QueryWrapper<AdminUser>().eq("username", username));
        if(Objects.isNull(adminUser)){
            throw new RuntimeException("用户名不存在");
        }else{
            // 通过认证后，封装成 UserDetail 对象
            // 调用 authentic 进行校验账号密码
            // 查找用户的权限信息
            List<String> permission = roleMapper.selectPermsInAdmin(adminUser.getId());
            UserDetails adminUserDetails = new AdminUserDetails(adminUser,permission);
            return adminUserDetails;
        }
    }

}
