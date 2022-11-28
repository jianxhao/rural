package com.rural.controller;

import com.rural.pojo.FraudInfo;
import com.rural.pojo.ResponseResult;
import com.rural.service.FraudInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author xh
 * @create 2022-11-27  16:44
 */
@RestController
@RequestMapping("/fraud")
public class FraudInfoController {
    @Autowired
    private FraudInfoService fraudInfoService;

    /**
     * 添加诈骗知识
     * @param fraudInfo
     * @return ResponseResult(code,msg)
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult insertFraudInfo(@RequestBody FraudInfo fraudInfo){
        return fraudInfoService.insertFraudInfo(fraudInfo);
    }

    /**
     * 删除诈骗知识
     * @param id
     * @return ResponseResult(code,msg)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult deleteFraudInfo(@PathVariable("id") Integer id){
        return fraudInfoService.deleteFraudInfo(id);
    }

    /**
     * 修改诈骗知识
     * @param fraudInfo
     * @return 返回ResponseResult(code,msg)
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updateFraudInfoInfo(@RequestBody FraudInfo fraudInfo){
        return fraudInfoService.updateFraudInfo(fraudInfo);
    }

    /**
     * 发布/下架 诈骗知识
     * @param id
     * @return 返回ResponseResult(code,msg)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult pubFraudInfo(@PathVariable("id") Integer id){
        return fraudInfoService.pubFraudInfo(id);
    }

    /**
     * 客服端查询诈骗知识
     * @param id
     * @return 返回ResponseResult(code,msg,data)
     */
    @GetMapping("/{id}")
    public ResponseResult selectFraudInfoById(@PathVariable("id") Integer id){
        return fraudInfoService.selectFraudInfoById(id);
    }

    /**
     * 后台查询诈骗知识
     * @param current
     * @param size
     * @param msg
     * @param type
     * @return 返回ResponseResult(code,msg，data)
     */
    @GetMapping("/{size}/{current}")
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult selectAllFraudInfo(@PathVariable("size") Integer size,
                                               @PathVariable("current") Integer current,
                                               @RequestParam String msg,
                                               @RequestParam String type){
        return fraudInfoService.selectAllFraudInfo(size,current,msg,type);
    }

    /**
     * 客服端诈骗知识显示
     * @param current 当前页码
     * @param size 每页记录数
     * @param id 诈骗知识id
     * @return 返回ResponseResult(code,msg,data)
     */
    @GetMapping("/wx/{size}/{current}/{id}")
    public ResponseResult selectFraudInfoByStatus(@PathVariable("size") Integer size,
                                                    @PathVariable("current") Integer current,
                                                    @PathVariable("id") Integer id){
        return fraudInfoService.selectFraudInfoByStatus(size,current,id);
    }
}
