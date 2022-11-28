package com.rural.controller;

import com.rural.pojo.FarmingInfo;
import com.rural.pojo.ResponseResult;
import com.rural.service.FarmingInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author xh
 * @create 2022-11-20  19:04
 */
@RestController
@RequestMapping("/farming")
public class FarmingInfoController {
    @Autowired
    private FarmingInfoService farmingInfoService;

    /**
     * 添加农业知识
     * @param farmingInfo
     * @return ResponseResult(code,msg)
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult insertFarmingInfo(@RequestBody FarmingInfo farmingInfo){
        return farmingInfoService.insertFarmingInfo(farmingInfo);
    }

    /**
     * 删除农业知识
     * @param id
     * @return ResponseResult(code,msg)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult deleteFarmingInfo(@PathVariable("id") Integer id){
        return farmingInfoService.deleteFarmingInfo(id);
    }

    /**
     * 修改农业知识
     * @param farmingInfo
     * @return 返回ResponseResult(code,msg)
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updateFarmingInfo(@RequestBody FarmingInfo farmingInfo){
        return farmingInfoService.updateFarmingInfo(farmingInfo);
    }
    /**
     * 发布/下架 农业知识
     * @param id
     * @return 返回ResponseResult(code,msg)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult pubFarmingInfo(@PathVariable("id") Integer id){
        return farmingInfoService.pubFarmingInfo(id);
    }

    /**
     * 客服端查询农业知识
     * @param id
     * @return 返回ResponseResult(code,msg,data)
     */
    @GetMapping("/{id}")
    public ResponseResult selectFarmingInfoById(@PathVariable("id") Integer id){
        return farmingInfoService.selectFarmingInfoById(id);
    }

    /**
     * 客服端农业知识显示
     * @param current
     * @param size
     * @param msg
     * @param type
     * @return 返回ResponseResult(code,msg,data)
     */
    @GetMapping("/wx/{size}/{current}")
    public ResponseResult selectFarmingInfoByStatus(@PathVariable("size") Integer size,
                                                    @PathVariable("current") Integer current,
                                                    @RequestParam String msg,
                                                    @RequestParam String type){
        return farmingInfoService.selectFarmingInfoByStatus(size,current,msg,type);
    }

    /**
     * 后台查询农业知识
     * @param current
     * @param size
     * @param msg
     * @param type
     * @return 返回ResponseResult(code,msg，data)
     */
    @GetMapping("/{size}/{current}")
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult selectAllFarmingInfo(@PathVariable("size") Integer size,
                                               @PathVariable("current") Integer current,
                                               @RequestParam String msg,
                                               @RequestParam String type){
        return farmingInfoService.selectAllFarmingInfo(size,current,msg,type);
    }
}
