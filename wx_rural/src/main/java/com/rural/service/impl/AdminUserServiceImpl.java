package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.pojo.*;
import com.rural.service.AdminUserService;
import com.rural.mapper.AdminUserMapper;
import com.rural.service.LoggerService;
import com.rural.utils.JwtUtil;
import com.rural.utils.PageInfoUtils;
import com.rural.utils.RedisCache;
import com.rural.utils.SecurityContextHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class AdminUserServiceImpl extends ServiceImpl<AdminUserMapper, AdminUser>
    implements AdminUserService
{
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AdminUserMapper adminUserMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private LoggerService loggerService;

    /**
     *
     * @param adminUser
     * @return ResponseResult(code,msg,data)
     * 登录状况验证
     */
    @Override
    public ResponseResult adminLogin(AdminUser adminUser) {
        // 通过账号密码进行校验
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(adminUser.getUsername(),adminUser.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        // 判断 authenticate 是否为空
        if(Objects.isNull(authenticate)){
            // 为空，账号密码错误
            return new ResponseResult(201,"账号或密码错误");
        }
        // 认证通过后，获取userId，生成token，返回token,保存Redis
        // 获取userId
        AdminUserDetails adminUserDetails = (AdminUserDetails) authenticate.getPrincipal();
        String userId = adminUserDetails.getAdminUser().getId().toString();
        // 生成ht_token
        String jwt = JwtUtil.createJWT(userId);
        Map<String,String> map = new HashMap<>();
        map.put("ht_token",jwt);
        // 把AdminUserDetails存储到redis
        redisCache.setCacheObject("login_ht:"+userId,adminUserDetails,30, TimeUnit.DAYS);
        // 返回登录提示给前端
        return new ResponseResult(200,"登录成功",map);

    }

    // 后台退出接口
    @Override
    public ResponseResult logout() {
        // 获取id
        Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
        // 在redis删除id
        redisCache.deleteObject("login_ht:"+userId);
        return new ResponseResult(200,"注销成功");
    }


    /**
     * 获取用户名和头像
     * @return ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectLoginAdmin() {
        // 获取id
        Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
        // 根据id查询个人信息
        AdminUser adminUser = adminUserMapper.selectById(userId);
        if(Objects.isNull(adminUser)){
            return new ResponseResult(201,"查询失败");
        }
        //
        Map<String,Object> map = new HashMap<>();
        map.put("name",adminUser.getUsername());
        map.put("avatarUrl",adminUser.getAvatar());
        return new ResponseResult(200,"查询成功",map);
    }

    /**
     * 获取用户个人信息
     * @return ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectById() {
        // 获取userId
        Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
        if(Objects.isNull(userId)){
            return new ResponseResult(202,"用户未登录");
        }
        // 根据用户id查询个人信息
        AdminUser adminUser = adminUserMapper.selectById(userId);
        if(Objects.isNull(adminUser)){
            return new ResponseResult(201,"查询失败");
        }
        // 返回信息给前端
        return new ResponseResult(200,"查询成功",adminUser);
    }

    /**
     * 查找所有用户
     * @param current
     * @param size
     * @param msg
     * @param type
     * @return ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectAll(Integer size,Integer current,String msg,String type) {
        Page<AdminUser> page = new Page<>(current,size);
        Page<AdminUser> adminPage = null;
        if(!StringUtils.hasText(msg)){
            adminPage = adminUserMapper.selectPage(page, null);
        }else {
            if(!StringUtils.hasText(type)){
                // 默认值
                type = "username";
            }
            // 按条件查询
            QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
            wrapper.like(type,msg);
            adminPage = adminUserMapper.selectPage(page, wrapper);
        }
        // 判断 adminPage 是否为空
        if(Objects.isNull(adminPage)){
            return new ResponseResult(201,"查询失败");
        }
        // 获取数据
        List<AdminUser> records = adminPage.getRecords();
        Map<String,Object> map = new HashMap<>();
        map.put("info",records);
        Map<String, Object> pageInfo1 = PageInfoUtils.getPageInfo(adminPage);
        map.put("pageInfo",pageInfo1);
        // 返回
        return new ResponseResult(200,"查询成功",map);
    }

    /**
     * 用户修改密码
     * @param oldPassword
     * @param newPassword
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult updatePassword(String oldPassword, String newPassword) {
        // 获取用户id
        Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
        // 获取密码
        AdminUser adminUser = adminUserMapper.selectById(userId);
        String password = adminUser.getPassword();
        // 判断用户密码是否正确
        boolean matches = passwordEncoder.matches(oldPassword, password);
        if(matches == false){
            return new ResponseResult(202,"旧密码错误");
        }
        // 密码正确加密修改
        String encode = passwordEncoder.encode(newPassword);
        adminUser.setPassword(encode);
        int result = adminUserMapper.updateById(adminUser);
        if(result > 0){
            return new ResponseResult(200,"修改成功");
        }
        return new ResponseResult(201,"修改失败");
    }

    /**
     * 修改用户信息
     * @param adminUser
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult updateUser(AdminUser adminUser) {
        // 获取用户信息
        AdminUser adminUser2 = adminUserMapper.selectById(adminUser.getId());
        int result = 0;
        // 判断用户名是否为空
        if(!Objects.isNull(adminUser.getUsername()) && !adminUser.getUsername().equals(adminUser2.getUsername())){
            // 判断用户名是否存在
            QueryWrapper<AdminUser> wrapper = new QueryWrapper<>();
            wrapper.eq("username",adminUser.getUsername());
            AdminUser adminUser1 = adminUserMapper.selectOne(wrapper);
            if(!Objects.isNull(adminUser1)){
                return new ResponseResult(202,"用户名已存在");
            }
        }
        if(!Objects.isNull(adminUser.getPassword())){
            adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
        }
        result = adminUserMapper.updateById(adminUser);
        // 判断result
        if(result > 0){
            return new ResponseResult(200,"修改成功");
        }
        return new ResponseResult(201,"修改失败");
    }
    /**
     * 删除用户
     * @param id
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult deleteUser(Integer id) {
        // 删除用户时，先把logger日志信息删除
        loggerService.deleteLogger(id);
        // 在redis移除
        Object cacheObject = redisCache.getCacheObject("login_ht:" + id);
        if(!Objects.isNull(cacheObject)){
            redisCache.deleteObject("login_ht:" + id);
        }
        // 删除
        int result = adminUserMapper.deleteById(id);
        if(result > 0 ) {
            return new ResponseResult(200,"删除成功");
        }
        return new ResponseResult(201,"删除失败");
    }

    /**
     * 修改用户信息(个人)
     * @param adminUser
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult updateUserInMy(AdminUser adminUser) {
        // 个人修改信息时不能更改用户名
        // 获取id
        Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
        adminUser.setId(userId);
        // 直接修改
        int result = adminUserMapper.updateById(adminUser);
        if(result > 0){
            return new ResponseResult(200,"修改成功");
        }
        return new ResponseResult(201,"修改失败");
    }
    /**
     * 添加用户
     * @param adminUser
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult insertUser(AdminUser adminUser) {
        LocalDate localDate = LocalDate.now();
        adminUser.setCreateTime(localDate);
        //判断用户名是否存在
        AdminUser username = adminUserMapper.selectOne(new QueryWrapper<AdminUser>().eq("username", adminUser.getUsername()));
        if(Objects.isNull(username)){
            // 加密密码
            adminUser.setPassword(passwordEncoder.encode(adminUser.getPassword()));
            // 添加
            int result = adminUserMapper.insert(adminUser);
            if(result > 0){
                // 添加权限
                adminUserMapper.addPermission(adminUser.getId(),2);
                return new ResponseResult(200,"添加成功");
            }
            return new ResponseResult(201,"添加失败");
        }
        return new ResponseResult(202,"用户名已存在");
    }

    /**
     * 禁用功能
     * @param
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult updateStatus(Integer id) {
        // 获取status
        AdminUser adminUser = adminUserMapper.selectById(id);
        if(adminUser.getStatus() == 0){
            adminUser.setStatus(1);
        }else{
            adminUser.setStatus(0);
        }
        int result = adminUserMapper.updateById(adminUser);
        if(result > 0) {
            return new ResponseResult(200,"禁用或启用成功");
        }
        return new ResponseResult(201,"禁用或启用失败");
    }


}





