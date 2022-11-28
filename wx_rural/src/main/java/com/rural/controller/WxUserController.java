package com.rural.controller;

import com.rural.pojo.ResponseResult;
import com.rural.pojo.WxUser;
import com.rural.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author xh
 * @create 2022-11-19  9:55
 */
@RestController
@RequestMapping("/wx")
public class WxUserController {

    @Autowired
    private WxUserService wxUserService;

    /**
     * 修改用户个人信息
     * @param wxUser
     * @return ResponseResult(code,msg)
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('client','admin','root')")
    public ResponseResult updateWxInfo(@RequestBody WxUser wxUser){
        return wxUserService.updateWxInfo(wxUser);
    }

    /**
     * 查找用户个人信息
     * @return ResponseResult(code,msg,data)
     */
    @GetMapping("/my")
    @PreAuthorize("hasAnyAuthority('client','admin','root')")
    public ResponseResult selectWxInfoOne(){
        return wxUserService.selectWxInfoOne();
    }

    /**
     * 查找用户信息
     * @param current
     * @param size
     * @param msg
     * @param type
     * @return ResponseResult(code,msg,data)
     */
    @GetMapping("/{size}/{current}")
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult selectWxInfo(@PathVariable("size") Integer size,
                                       @PathVariable("current") Integer current,
                                       @RequestParam String msg,
                                       @RequestParam String type){
        return wxUserService.selectWxInfo(size,current,msg,type);
    }


    // TODO 消息订阅待开发
    @GetMapping("/send")
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult send(){
        return wxUserService.send();
    }
}
