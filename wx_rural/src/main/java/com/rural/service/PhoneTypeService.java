package com.rural.service;

import com.rural.pojo.PhoneType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rural.pojo.ResponseResult;

/**
 *
 */
public interface PhoneTypeService extends IService<PhoneType> {

    ResponseResult selectAllPhoneType();

    ResponseResult insertPhoneType(PhoneType phoneType);

    ResponseResult updatePhoneType(PhoneType phoneType);

    ResponseResult deletePhoneType(Integer id);

}
