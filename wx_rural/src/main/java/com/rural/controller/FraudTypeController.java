package com.rural.controller;

import com.rural.pojo.FraudType;
import com.rural.pojo.ResponseResult;
import com.rural.service.FraudTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
/**
 * @author xh
 * @create 2022-11-27  16:19
 */
@RestController
@RequestMapping("/fraud/type")
public class FraudTypeController {

    @Autowired
    private FraudTypeService fraudTypeService;

    /**
     * 查询(后台)
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult selectAllFraudType(){
        return fraudTypeService.selectAllFraudType();
    }

    /**
     * 查询(微信)
     * @return
     */
    @GetMapping("/wx")
    public ResponseResult selectAllFraudTypeWx(){
        return fraudTypeService.selectAllFraudTypeWx();
    }
    /**
     * 添加
     * @param fraudType
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult insertFraudType(@RequestBody FraudType fraudType){
        return fraudTypeService.insertFraudType(fraudType);
    }

    /**
     * 修改
     * @param fraudType
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updateFraudType(@RequestBody FraudType fraudType){
        return fraudTypeService.updateFraudType(fraudType);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult deleteFraudType(@PathVariable("id") Integer id){
        return fraudTypeService.deleteFraudType(id);
    }

    /**
     * 发布/下架
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updateFraudTypeInStatus(@PathVariable("id") Integer id){
        return fraudTypeService.updateFraudTypeInStatus(id);
    }
}
