package com.rural.service;

import com.rural.pojo.HotspotInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rural.pojo.ResponseResult;

/**
 *
 */
public interface HotspotInfoService extends IService<HotspotInfo> {

    ResponseResult insertHotspotInfo(HotspotInfo hotspotInfo);

    ResponseResult deleteHotspotInfo(Integer id);

    ResponseResult pubHotspotInfo(Integer id);

    ResponseResult selectHotspotInfoById(Integer id);

    ResponseResult selectAllHotspotInfo(Integer size, Integer current, String msg, String type);

    ResponseResult selectHotspotInfoByStatus(Integer size, Integer current, Integer id);

    ResponseResult updateHotspotInfo(HotspotInfo hotspotInfo);
}
