package com.rural.controller;

import com.rural.pojo.AdminUser;
import com.rural.pojo.ResponseResult;
import com.rural.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author xh
 * @create 2022-11-19  9:55
 */
@RestController
@RequestMapping("/admin")
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;

    /**
     * 获取用户名和头像
     * @return ResponseResult(code,msg,data)
     */
    @GetMapping("/info")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult selectLoginAdmin(){
        return adminUserService.selectLoginAdmin();
    }

    /**
     * 获取用户个人信息
     * @return ResponseResult(code,msg,data)
     */
    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult selectById(){
        return adminUserService.selectById();
    }

    /**
     * 查找所有用户
     * @param current
     * @param size
     * @param msg
     * @param type
     * @return ResponseResult(code,msg,data)
     */
    @GetMapping("/{size}/{current}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult selectAllUser(@PathVariable("size") Integer size,
                                        @PathVariable("current") Integer current,
                                        @RequestParam String msg,
                                        @RequestParam String type){
        return adminUserService.selectAll(size,current,msg,type);
    }

    /**
     * 用户修改密码
     * @param oldPassword
     * @param newPassword
     * @return ResponseResult(code,msg)
     */
    @PutMapping("/password")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updatePassword(@RequestParam String oldPassword,@RequestParam String newPassword){
        return adminUserService.updatePassword(oldPassword, newPassword);
    }

    /**
     * 修改用户信息(超级管理员)
     * @param adminUser
     * @return ResponseResult(code,msg)
     */
    @PutMapping
    @PreAuthorize("hasAuthority('root')")
    public ResponseResult updateUser(@RequestBody AdminUser adminUser){
        return adminUserService.updateUser(adminUser);
    }
    /**
     * 修改用户信息(个人)
     * @param adminUser
     * @return ResponseResult(code,msg)
     */
    @PutMapping("/my")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updateUserInMy(@RequestBody AdminUser adminUser){
        return adminUserService.updateUserInMy(adminUser);
    }

    /**
     * 删除用户
     * @param id
     * @return ResponseResult(code,msg)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('root')")
    public ResponseResult deleteUser(@PathVariable("id") Integer id){
        return adminUserService.deleteUser(id);
    }

    /**
     * 添加用户
     * @param adminUser
     * @return ResponseResult(code,msg)
     */
    @PostMapping
    @PreAuthorize("hasAuthority('root')")
    public ResponseResult insertUser(@RequestBody AdminUser adminUser){
        return adminUserService.insertUser(adminUser);
    }

    /**
     * 禁用功能
     * @param
     * @return ResponseResult(code,msg)
     */
    @PostMapping("/status/{id}")
    @PreAuthorize("hasAuthority('root')")
    public ResponseResult updateStatus(@PathVariable("id") Integer id){
        return adminUserService.updateStatus(id);
    }

}
