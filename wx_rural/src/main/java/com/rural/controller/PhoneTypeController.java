package com.rural.controller;

import com.rural.pojo.PhoneType;
import com.rural.pojo.ResponseResult;
import com.rural.service.PhoneTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author xh
 * @create 2022-11-24  22:01
 */
@RestController
@RequestMapping("/phone/type")
public class PhoneTypeController {

    @Autowired
    private PhoneTypeService phoneTypeService;

    /**
     * 查询
     * @return
     */
    @GetMapping
    public ResponseResult selectAllPhoneType(){
        return phoneTypeService.selectAllPhoneType();
    }

    /**
     * 添加
     * @param phoneType
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult insertPhoneType(@RequestBody PhoneType phoneType){
        return phoneTypeService.insertPhoneType(phoneType);
    }

    /**
     * 修改
     * @param phoneType
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updatePhoneType(@RequestBody PhoneType phoneType){
        return phoneTypeService.updatePhoneType(phoneType);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult deletePhoneType(@PathVariable("id") Integer id){
        return phoneTypeService.deletePhoneType(id);
    }
}
