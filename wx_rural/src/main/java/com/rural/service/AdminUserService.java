package com.rural.service;

import com.rural.pojo.AdminUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rural.pojo.ResponseResult;

/**
 *
 */
public interface AdminUserService extends IService<AdminUser> {

    ResponseResult adminLogin(AdminUser adminUser);

    ResponseResult logout();

    ResponseResult selectLoginAdmin();

    ResponseResult selectById();

    ResponseResult selectAll(Integer size,Integer current,String msg,String type);

    ResponseResult updatePassword(String oldPassword, String newPassword);

    ResponseResult updateUser(AdminUser adminUser);

    ResponseResult deleteUser(Integer id);

    ResponseResult updateUserInMy(AdminUser adminUser);

    ResponseResult insertUser(AdminUser adminUser);

    ResponseResult updateStatus(Integer id);
}
