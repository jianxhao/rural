package com.rural.service;

import com.rural.pojo.FarmingInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rural.pojo.ResponseResult;

/**
 *
 */
public interface FarmingInfoService extends IService<FarmingInfo> {

    ResponseResult insertFarmingInfo(FarmingInfo farmingInfo);

    ResponseResult deleteFarmingInfo(Integer id);

    ResponseResult updateFarmingInfo(FarmingInfo farmingInfo);

    ResponseResult pubFarmingInfo(Integer id);

    ResponseResult selectFarmingInfoById(Integer id);

    ResponseResult selectFarmingInfoByStatus(Integer size,Integer current,String msg,String type);

    ResponseResult selectAllFarmingInfo(Integer size,Integer current,String msg,String type);
}
