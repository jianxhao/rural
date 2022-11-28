package com.rural.service;

import com.rural.pojo.ResponseResult;
import com.rural.pojo.WxInfo;
import com.rural.pojo.WxUser;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface WxUserService extends IService<WxUser> {
    WxUser getLoginCertificate(String code,String rawData) throws Exception;

    ResponseResult getWxUserInfo(WxInfo wxInfo) throws Exception;

    ResponseResult logout();

    ResponseResult updateWxInfo(WxUser wxUser);

    ResponseResult selectWxInfoOne();

    ResponseResult selectWxInfo(Integer size,Integer current,String msg,String type);

    ResponseResult send();

}
