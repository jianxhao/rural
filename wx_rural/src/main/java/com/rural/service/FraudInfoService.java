package com.rural.service;

import com.rural.pojo.FraudInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rural.pojo.ResponseResult;

/**
 *
 */
public interface FraudInfoService extends IService<FraudInfo> {

    ResponseResult insertFraudInfo(FraudInfo fraudInfo);

    ResponseResult deleteFraudInfo(Integer id);

    ResponseResult updateFraudInfo(FraudInfo fraudInfo);

    ResponseResult pubFraudInfo(Integer id);

    ResponseResult selectFraudInfoById(Integer id);

    ResponseResult selectAllFraudInfo(Integer size, Integer current, String msg, String type);

    ResponseResult selectFraudInfoByStatus(Integer size, Integer current, Integer id);
}
