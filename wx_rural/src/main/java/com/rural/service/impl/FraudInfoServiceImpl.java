package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.mapper.FraudInfoMapper;
import com.rural.pojo.*;
import com.rural.service.FraudInfoService;
import com.rural.service.LoggerService;
import com.rural.utils.PageInfoUtils;
import com.rural.utils.SecurityContextHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
@Service
public class FraudInfoServiceImpl extends ServiceImpl<FraudInfoMapper, FraudInfo>
    implements FraudInfoService{
    private final static String TYPE_NAME = "诈骗知识";

    @Autowired
    private FraudInfoMapper fraudInfoMapper;
    @Autowired
    private LoggerService loggerService;


    /**
     * 添加诈骗知识
     * @param fraudInfo
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult insertFraudInfo(FraudInfo fraudInfo) {
        // 创建时间
        LocalDate localDate = LocalDate.now();
        // 添加时，创建时间和更新时间一致
        fraudInfo.setCreateTime(localDate);
        fraudInfo.setUpdateTime(localDate);

        try {
            int result = fraudInfoMapper.insert(fraudInfo);
            if(result > 0){
                // 添加日志信息
                Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
                Logger logger = new Logger();
                logger.setUserId(userId);
                logger.setModule(TYPE_NAME);
                logger.setNode("添加诈骗知识id: "+ fraudInfo.getId());
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
     * 删除诈骗知识
     * @param id
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult deleteFraudInfo(Integer id) {
        int result = fraudInfoMapper.deleteById(id);
        if(result > 0){
            // 添加日志信息
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("删除诈骗知识id: "+ id);
            loggerService.insertLoggerInfo(logger);
            // 返回
            return new ResponseResult(200,"删除成功");
        }
        return new ResponseResult(201,"删除失败");
    }

    /**
     * 修改诈骗知识
     * @param fraudInfo
     * @return 返回ResponseResult(code,msg)
     */
    @Override
    public ResponseResult updateFraudInfo(FraudInfo fraudInfo) {
        LocalDate localDate = LocalDate.now();
        fraudInfo.setUpdateTime(localDate);
        try {
            // 执行修改操作
            int result = fraudInfoMapper.updateById(fraudInfo);
            if(result > 0){
                // 添加日志信息
                Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
                Logger logger = new Logger();
                logger.setUserId(userId);
                logger.setModule(TYPE_NAME);
                logger.setNode("修改诈骗知识ID: "+fraudInfo.getId());
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
     * 发布/下架 诈骗知识
     * @param id
     * @return 返回ResponseResult(code,msg)
     */
    @Override
    public ResponseResult pubFraudInfo(Integer id) {
        String status = "发布";
        Integer statusNum = 0;
        // 查找当前农业知识的状态
        FraudInfo fraudInfo = fraudInfoMapper.selectById(id);
        if(fraudInfo.getStatus() == 0){
            status = "下架";
            statusNum = 1;
        }

        UpdateWrapper<FraudInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id)
                .set("status",statusNum);
        int result = fraudInfoMapper.update(null, updateWrapper);
        if(result > 0){
            // 添加日志信息
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode(status+"诈骗知识ID: "+id);
            loggerService.insertLoggerInfo(logger);
            return new ResponseResult(200,status+"成功");
        }else{
            return new ResponseResult(201,status+"失败");
        }
    }
    /**
     * 客服端查询诈骗知识
     * @param id
     * @return 返回ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectFraudInfoById(Integer id) {
        try {
            FraudInfo fraudInfo = fraudInfoMapper.selectById(id);
            Map<String,Object> map = null;
            if(!Objects.isNull(fraudInfo)) {
                map.put("context",fraudInfo.getNote());
            }
            return new ResponseResult(200, "查询成功", map);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }
    }
    /**
     * 后台查询诈骗知识
     * @param current
     * @param size
     * @param msg
     * @param type
     * @return 返回ResponseResult(code,msg，data)
     */
    @Override
    public ResponseResult selectAllFraudInfo(Integer size, Integer current, String msg, String type) {
        Page<FraudInfo> page = new Page<>(current,size);
        Page<FraudInfo> fraudInfoPage = null;
        if(!StringUtils.hasText(type)){
            // 默认值
            type = "title";
        }
        // msg为空时查找所有
        if(!StringUtils.hasText(msg)){
            fraudInfoPage = fraudInfoMapper.selectPageAll(page, type,null);
        }else{
            // 模糊查找
            fraudInfoPage = fraudInfoMapper.selectPageAll(page,type,msg);
        }
        if(Objects.isNull(fraudInfoPage)){
            return new ResponseResult(201,"查询失败");
        }
        List<FraudInfo> records = fraudInfoPage.getRecords();
        // 返回数据
        Map<String,Object> maps = new HashMap<>();
        maps.put("info",records);
        Map<String, Object> pageInfo1 = PageInfoUtils.getPageInfo(fraudInfoPage);
        maps.put("pageInfo",pageInfo1);
        return new ResponseResult(200,"查询成功",maps);
    }

    /**
     * 客服端诈骗知识显示
     * @param current
     * @param size
     * @param id
     * @return 返回ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectFraudInfoByStatus(Integer size, Integer current, Integer id) {
        Page<FraudInfo> page = new Page<>(current,size);
        // 根据typeId查询
        Page<FraudInfo> fraudInfoPage = fraudInfoMapper.selectListById(page,id);
        // 判断 fraudInfoPage 是否为空
        if(Objects.isNull(fraudInfoPage)){
            return new ResponseResult(201,"查询失败");
        }
        List<FraudInfo> records = fraudInfoPage.getRecords();
        // 返回数据
        Map<String,Object> maps = new HashMap<>();
        maps.put("info",records);
        Map<String, Object> pageInfo1 = PageInfoUtils.getPageInfo(fraudInfoPage);
        maps.put("pageInfo",pageInfo1);
        return new ResponseResult(200,"查询成功",maps);
    }

}




