package com.rural.controller;

import com.rural.pojo.HotspotInfo;
import com.rural.pojo.ResponseResult;
import com.rural.service.HotspotInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author xh
 * @create 2022-11-28  19:12
 */
@RestController
@RequestMapping("/hotspot")
public class HotspotInfoController {
    @Autowired
    private HotspotInfoService hotspotInfoService;

    /**
     * 添加热点信息
     * @param hotspotInfo
     * @return ResponseResult(code,msg)
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult insertFraudInfo(@RequestBody HotspotInfo hotspotInfo){
        return hotspotInfoService.insertHotspotInfo(hotspotInfo);
    }

    /**
     * 删除热点信息
     * @param id
     * @return ResponseResult(code,msg)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult deleteHotspotInfo(@PathVariable("id") Integer id){
        return hotspotInfoService.deleteHotspotInfo(id);
    }

    /**
     * 修改热点信息
     * @param hotspotInfo
     * @return 返回ResponseResult(code,msg)
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updateFraudInfoInfo(@RequestBody HotspotInfo hotspotInfo){
        return hotspotInfoService.updateHotspotInfo(hotspotInfo);
    }

    /**
     * 发布/下架热点信息
     * @param id
     * @return 返回ResponseResult(code,msg)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult pubHotspotInfo(@PathVariable("id") Integer id){
        return hotspotInfoService.pubHotspotInfo(id);
    }

    /**
     * 客服端查询热点信息
     * @param id
     * @return 返回ResponseResult(code,msg,data)
     */
    @GetMapping("/{id}")
    public ResponseResult selectHotspotInfoById(@PathVariable("id") Integer id){
        return hotspotInfoService.selectHotspotInfoById(id);
    }

    /**
     * 后台查询热点信息
     * @param current
     * @param size
     * @param msg
     * @param type
     * @return 返回ResponseResult(code,msg，data)
     */
    @GetMapping("/{size}/{current}")
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult selectAllHotspotInfo(@PathVariable("size") Integer size,
                                             @PathVariable("current") Integer current,
                                             @RequestParam String msg,
                                             @RequestParam String type){
        return hotspotInfoService.selectAllHotspotInfo(size,current,msg,type);
    }

    /**
     * 客服端热点信息显示
     * @param current 当前页码
     * @param size 每页记录数
     * @param id 热点信息id
     * @return 返回ResponseResult(code,msg,data)
     */
    @GetMapping("/wx/{size}/{current}/{id}")
    public ResponseResult selectHotspotInfoByStatus(@PathVariable("size") Integer size,
                                                  @PathVariable("current") Integer current,
                                                  @PathVariable("id") Integer id){
        return hotspotInfoService.selectHotspotInfoByStatus(size,current,id);
    }
}
