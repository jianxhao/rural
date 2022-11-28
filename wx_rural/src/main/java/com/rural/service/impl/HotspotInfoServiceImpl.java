package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.pojo.HotspotInfo;
import com.rural.pojo.Logger;
import com.rural.pojo.ResponseResult;
import com.rural.service.HotspotInfoService;
import com.rural.mapper.HotspotInfoMapper;
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
public class HotspotInfoServiceImpl extends ServiceImpl<HotspotInfoMapper, HotspotInfo>
    implements HotspotInfoService{
    private final static String TYPE_NAME = "热点信息";

    @Autowired
    private HotspotInfoMapper hotspotInfoMapper;
    @Autowired
    private LoggerService loggerService;
    /**
     * 添加热点知识
     * @param hotspotInfo
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult insertHotspotInfo(HotspotInfo hotspotInfo) {
        // 创建时间
        LocalDate localDate = LocalDate.now();
        // 添加时，创建时间和更新时间一致
        hotspotInfo.setCreateTime(localDate);
        hotspotInfo.setUpdateTime(localDate);

        try {
            int result = hotspotInfoMapper.insert(hotspotInfo);
            if(result > 0){
                // 添加日志信息
                Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
                Logger logger = new Logger();
                logger.setUserId(userId);
                logger.setModule(TYPE_NAME);
                logger.setNode("添加热点信息id: "+ hotspotInfo.getId());
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
     * 删除热点信息
     * @param id
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult deleteHotspotInfo(Integer id) {
        int result = hotspotInfoMapper.deleteById(id);
        if(result > 0){
            // 添加日志信息
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("删除热点信息id: "+ id);
            loggerService.insertLoggerInfo(logger);
            // 返回
            return new ResponseResult(200,"删除成功");
        }
        return new ResponseResult(201,"删除失败");
    }
    /**
     * 客服端查询热点信息
     * @param id
     * @return 返回ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectHotspotInfoById(Integer id) {
        try {
            HotspotInfo hotspotInfo = hotspotInfoMapper.selectById(id);
            Map<String,Object> map = null;
            if(!Objects.isNull(hotspotInfo)) {
                map.put("context",hotspotInfo.getNote());
            }
            return new ResponseResult(200, "查询成功", map);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }
    }
    /**
     * 后台查询热点信息
     * @param current
     * @param size
     * @param msg
     * @param type
     * @return 返回ResponseResult(code,msg，data)
     */
    @Override
    public ResponseResult selectAllHotspotInfo(Integer size, Integer current, String msg, String type) {
        Page<HotspotInfo> page = new Page<>(current,size);
        Page<HotspotInfo> hotspotInfoPage = null;
        if(!StringUtils.hasText(type)){
            // 默认值
            type = "title";
        }
        // msg为空时查找所有
        if(!StringUtils.hasText(msg)){
            hotspotInfoPage = hotspotInfoMapper.selectPageAll(page, type,null);
        }else{
            // 模糊查找
            hotspotInfoPage = hotspotInfoMapper.selectPageAll(page,type,msg);
        }
        if(Objects.isNull(hotspotInfoPage)){
            return new ResponseResult(201,"查询失败");
        }
        List<HotspotInfo> records = hotspotInfoPage.getRecords();
        // 返回数据
        Map<String,Object> maps = new HashMap<>();
        maps.put("info",records);
        Map<String, Object> pageInfo1 = PageInfoUtils.getPageInfo(hotspotInfoPage);
        maps.put("pageInfo",pageInfo1);
        return new ResponseResult(200,"查询成功",maps);
    }
    /**
     * 客服端热点信息显示
     * @param current 当前页码
     * @param size 每页记录数
     * @param id 热点信息id
     * @return 返回ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectHotspotInfoByStatus(Integer size, Integer current, Integer id) {
        Page<HotspotInfo> page = new Page<>(current,size);
        // 根据typeId查询
        Page<HotspotInfo> hotspotInfoPage = hotspotInfoMapper.selectListById(page,id);
        // 判断 fraudInfoPage 是否为空
        if(Objects.isNull(hotspotInfoPage)){
            return new ResponseResult(201,"查询失败");
        }
        List<HotspotInfo> records = hotspotInfoPage.getRecords();
        // 返回数据
        Map<String,Object> maps = new HashMap<>();
        maps.put("info",records);
        Map<String, Object> pageInfo1 = PageInfoUtils.getPageInfo(hotspotInfoPage);
        maps.put("pageInfo",pageInfo1);
        return new ResponseResult(200,"查询成功",maps);
    }
    /**
     * 修改热点信息
     * @param hotspotInfo
     * @return 返回ResponseResult(code,msg)
     */
    @Override
    public ResponseResult updateHotspotInfo(HotspotInfo hotspotInfo) {
        LocalDate localDate = LocalDate.now();
        hotspotInfo.setUpdateTime(localDate);
        try {
            // 执行修改操作
            int result = hotspotInfoMapper.updateById(hotspotInfo);
            if(result > 0){
                // 添加日志信息
                Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
                Logger logger = new Logger();
                logger.setUserId(userId);
                logger.setModule(TYPE_NAME);
                logger.setNode("修改热点信息ID: "+hotspotInfo.getId());
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
     * 发布/下架 热点信息
     * @param id
     * @return 返回ResponseResult(code,msg)
     */
    @Override
    public ResponseResult pubHotspotInfo(Integer id) {
        String status = "发布";
        Integer statusNum = 0;
        // 查找当前农业知识的状态
        HotspotInfo hotspotInfo = hotspotInfoMapper.selectById(id);
        if(hotspotInfo.getStatus() == 0){
            status = "下架";
            statusNum = 1;
        }

        UpdateWrapper<HotspotInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",id)
                .set("status",statusNum);
        int result = hotspotInfoMapper.update(null, updateWrapper);
        if(result > 0){
            // 添加日志信息
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode(status+"热点信息ID: "+id);
            loggerService.insertLoggerInfo(logger);
            return new ResponseResult(200,status+"成功");
        }else{
            return new ResponseResult(201,status+"失败");
        }
    }
}




