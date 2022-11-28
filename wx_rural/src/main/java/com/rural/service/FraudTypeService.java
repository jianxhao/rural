package com.rural.service;

import com.rural.pojo.FraudType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rural.pojo.ResponseResult;

/**
 *
 */
public interface FraudTypeService extends IService<FraudType> {

    ResponseResult selectAllFraudType();

    ResponseResult insertFraudType(FraudType fraudType);

    ResponseResult updateFraudType(FraudType fraudType);

    ResponseResult deleteFraudType(Integer id);

    ResponseResult selectAllFraudTypeWx();

    ResponseResult updateFraudTypeInStatus(Integer id);
}
