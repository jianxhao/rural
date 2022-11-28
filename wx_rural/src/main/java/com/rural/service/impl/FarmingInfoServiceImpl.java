package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.pojo.*;
import com.rural.service.FarmingInfoService;
import com.rural.mapper.FarmingInfoMapper;
import com.rural.service.LoggerService;
import com.rural.utils.PageInfoUtils;
import com.rural.utils.SecurityContextHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;

/**
 *
 */
@Service
public class FarmingInfoServiceImpl extends ServiceImpl<FarmingInfoMapper, FarmingInfo>
    implements FarmingInfoService{

    private final static String TYPE_NAME = "农业知识";

    @Autowired
    private FarmingInfoMapper farmingInfoMapper;
    @Autowired
    private LoggerService loggerService;

    /**
     * 添加农业知识
     * @param farmingInfo
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult insertFarmingInfo(FarmingInfo farmingInfo) {
        // 创建时间
        LocalDate localDate = LocalDate.now();
        // 添加时，创建时间和更新时间一致
        farmingInfo.setCreateTime(localDate);
        farmingInfo.setUpdateTime(localDate);

        try {
            int result = farmingInfoMapper.insert(farmingInfo);
            if(result > 0){
                // 添加日志信息
                Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
                Logger logger = new Logger();
                logger.setUserId(userId);
                logger.setModule(TYPE_NAME);
                logger.setNode("添加农业知识id: "+ farmingInfo.getId());
                loggerService.insertLoggerInfo(logger);
                // 返回
                return new ResponseResult(200,"添加成功");
            }else{
                return new ResponseResult(201,"添加失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"添加失败");
        }
    }

    /**
     * 删除农业知识
     * @param id
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult deleteFarmingInfo(Integer id) {
        try {
            int result = farmingInfoMapper.deleteById(id);
            if(result > 0){
                // 添加日志信息
                Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
                Logger logger = new Logger();
                logger.setUserId(userId);
                logger.setModule(TYPE_NAME);
                logger.setNode("删除农业知识id: "+ id);
                loggerService.insertLoggerInfo(logger);
                // 返回
                return new ResponseResult(200,"删除成功");
            }else{
                return new ResponseResult(201,"删除失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"删除失败");
        }
    }

    /**
     * 修改农业知识
     * @param farmingInfo
     * @return 返回ResponseResult(code,msg)
     */
    @Override
    public ResponseResult updateFarmingInfo(FarmingInfo farmingInfo) {
        LocalDate localDate = LocalDate.now();
        farmingInfo.setUpdateTime(localDate);
        try {
            // 执行修改操作
            int result = farmingInfoMapper.updateById(farmingInfo);
            if(result > 0){
                // 添加日志信息
                Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
                Logger logger = new Logger();
                logger.setUserId(userId);
                logger.setModule(TYPE_NAME);
                logger.setNode("修改农业知识ID: "+farmingInfo.getId());
                loggerService.insertLoggerInfo(logger);
                return new ResponseResult(200,"修改成功");
            }else{
                return new ResponseResult(201,"修改失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"修改失败");
        }
    }

    /**
     * 发布/下架 农业知识
     * @param id
     * @return 返回ResponseResult(code,msg)
     */
    @Override
    public ResponseResult pubFarmingInfo(Integer id) {
        String status = "发布";
        Integer statusNum = 0;
        // 查找当前农业知识的状态
        FarmingInfo farmingInfo = farmingInfoMapper.selectById(id);
        if(farmingInfo.getStatus() == 0){
            status = "下架";
            statusNum = 1;
        }

        UpdateWrapper<FarmingInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id)
                .set("status",statusNum);
        int result = farmingInfoMapper.update(null, updateWrapper);
        if(result > 0){
            // 添加日志信息
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode(status+"农业知识ID: "+id);
            loggerService.insertLoggerInfo(logger);
            return new ResponseResult(200,status+"成功");
        }else{
            return new ResponseResult(201,status+"失败");
        }
    }

    /**
     * 客服端查询农业知识
     * @param id
     * @return 返回ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectFarmingInfoById(Integer id) {
        try {
            FarmingInfo farmingInfo = farmingInfoMapper.selectById(id);
            Map<String,Object> map = null;
            if(!Objects.isNull(farmingInfo)) {
                map.put("context",farmingInfo.getNote());
            }
            return new ResponseResult(200, "查询成功", map);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }
    }

    /**
     * 客服端农业知识显示
     * @return 返回ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectFarmingInfoByStatus(Integer size,Integer current,String msg,String type) {
        Page<FarmingInfo> page = new Page<>(current,size);
        Page<FarmingInfo> farmingPage = null;
        if(!StringUtils.hasText(msg)){
            QueryWrapper<FarmingInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("status",0);
            farmingPage = farmingInfoMapper.selectPage(page, wrapper);
        }else{
            if(!StringUtils.hasText(type)){
                // 默认值
                type = "title";
            }
            // 模糊查找
            QueryWrapper<FarmingInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("status",0)
                    .like(type,msg);
            farmingPage = farmingInfoMapper.selectPage(page, wrapper);
        }
        // 判断 farmingPage 是否为空
        if(Objects.isNull(farmingPage)){
            return new ResponseResult(201,"查询失败");
        }
        List<FarmingInfo> records = farmingPage.getRecords();
        // 返回数据
        Map<String,Object> maps = new HashMap<>();
        maps.put("info",records);
        Map<String, Object> pageInfo1 = PageInfoUtils.getPageInfo(farmingPage);
        maps.put("pageInfo",pageInfo1);
        return new ResponseResult(200,"查询成功",maps);

    }

    /**
     * 后台查询农业知识
     * @return 返回ResponseResult(code,msg，data)
     */
    @Override
    public ResponseResult selectAllFarmingInfo(Integer size,Integer current,String msg,String type) {
        Page<FarmingInfo> page = new Page<>(current,size);
        Page<FarmingInfo> farmingPage = null;
        // msg为空时查找所有
        if(!StringUtils.hasText(msg)){
            farmingPage = farmingInfoMapper.selectPage(page, null);
        }else{
            if(!StringUtils.hasText(type)){
                // 默认值
                type = "title";
            }
            // 模糊查找
            QueryWrapper<FarmingInfo> wrapper = new QueryWrapper<>();
            wrapper.like(type,msg);
            farmingPage = farmingInfoMapper.selectPage(page, wrapper);
        }
        if(Objects.isNull(farmingPage)){
            return new ResponseResult(201,"查询失败");
        }
        List<FarmingInfo> records = farmingPage.getRecords();
        // 返回数据
        Map<String,Object> maps = new HashMap<>();
        maps.put("info",records);
        Map<String, Object> pageInfo1 = PageInfoUtils.getPageInfo(farmingPage);
        maps.put("pageInfo",pageInfo1);
        return new ResponseResult(200,"查询成功",maps);
    }


}




