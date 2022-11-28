package com.rural.service;

import com.rural.pojo.HotspotType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rural.pojo.ResponseResult;

/**
 *
 */
public interface HotspotTypeService extends IService<HotspotType> {

    ResponseResult selectAllHotspotType();

    ResponseResult selectAllHotspotTypeWx();

    ResponseResult insertHotspotType(HotspotType hotspotType);

    ResponseResult updateHotspotType(HotspotType hotspotType);

    ResponseResult deleteHotspotType(Integer id);

    ResponseResult updateHotspotTypeInStatus(Integer id);
}
