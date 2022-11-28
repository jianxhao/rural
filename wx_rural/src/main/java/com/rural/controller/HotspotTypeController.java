package com.rural.controller;

import com.rural.pojo.HotspotType;
import com.rural.pojo.ResponseResult;
import com.rural.service.HotspotTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author xh
 * @create 2022-11-28  18:33
 */
@RestController
@RequestMapping("/hotspot/type")
public class HotspotTypeController {

    @Autowired
    private HotspotTypeService hotspotTypeService;

    /**
     * 查询(后台)
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult selectAllHotspotType(){
        return hotspotTypeService.selectAllHotspotType();
    }

    /**
     * 查询(微信)
     * @return
     */
    @GetMapping("/wx")
    public ResponseResult selectAllHotspotTypeWx(){
        return hotspotTypeService.selectAllHotspotTypeWx();
    }
    /**
     * 添加
     * @param hotspotType
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult insertHotspotType(@RequestBody HotspotType hotspotType){
        return hotspotTypeService.insertHotspotType(hotspotType);
    }

    /**
     * 修改
     * @param hotspotType
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updateHotspotType(@RequestBody HotspotType hotspotType){
        return hotspotTypeService.updateHotspotType(hotspotType);
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult deleteHotspotType(@PathVariable("id") Integer id){
        return hotspotTypeService.deleteHotspotType(id);
    }

    /**
     * 发布/下架
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updateHotspotTypeInStatus(@PathVariable("id") Integer id){
        return hotspotTypeService.updateHotspotTypeInStatus(id);
    }
}
