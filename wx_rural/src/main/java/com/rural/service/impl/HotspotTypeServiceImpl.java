package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.mapper.HotspotInfoMapper;
import com.rural.pojo.*;
import com.rural.service.HotspotTypeService;
import com.rural.mapper.HotspotTypeMapper;
import com.rural.service.LoggerService;
import com.rural.utils.SecurityContextHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 *
 */
@Service
public class HotspotTypeServiceImpl extends ServiceImpl<HotspotTypeMapper, HotspotType>
    implements HotspotTypeService{
    private final static String TYPE_NAME = "热点分类";
    @Autowired
    private HotspotTypeMapper hotspotTypeMapper;

    @Autowired
    private HotspotInfoMapper hotspotInfoMapper;

    @Autowired
    private LoggerService loggerService;
    /**
     * 查询(后台)
     * @return
     */
    @Override
    public ResponseResult selectAllHotspotType() {
        // 查询所有
        try {
            List<HotspotType> hotspotTypes = hotspotTypeMapper.selectList(null);
            return new ResponseResult(200,"查询成功",hotspotTypes);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }
    }
    /**
     * 查询(微信)
     * @return
     */
    @Override
    public ResponseResult selectAllHotspotTypeWx() {
        // 查询所有
        try {
            List<HotspotType> hotspotTypes = hotspotTypeMapper.selectList(new QueryWrapper<HotspotType>().eq("status",0));
            return new ResponseResult(200,"查询成功",hotspotTypes);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }
    }
    /**
     * 添加
     * @param hotspotType
     * @return
     */
    @Override
    public ResponseResult insertHotspotType(HotspotType hotspotType) {
        // 判断是否存在模块
        HotspotType typeName = hotspotTypeMapper.selectOne(new QueryWrapper<HotspotType>().eq("type_name", hotspotType.getTypeName()));
        if(!Objects.isNull(typeName)){
            return new ResponseResult(202,"分类名已存在");
        }
        // 创建时间
        LocalDate localDate = LocalDate.now();
        hotspotType.setCreateTime(localDate);
        hotspotType.setUpdateTime(localDate);
        // 添加
        int result = hotspotTypeMapper.insert(hotspotType);
        if(result > 0 ){
            // 添加日志信息
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("添加了分类名: "+ hotspotType.getTypeName());
            loggerService.insertLoggerInfo(logger);
            return new ResponseResult(200,"添加成功");
        }
        return new ResponseResult(201,"添加失败");
    }

    /**
     * 修改
     * @param hotspotType
     * @return
     */
    @Override
    public ResponseResult updateHotspotType(HotspotType hotspotType) {
        // 判断typeName是否为空
        if(StringUtils.hasText(hotspotType.getTypeName())){
            // 判断是否存在
            QueryWrapper<HotspotType> wrapper = new QueryWrapper<>();
            wrapper.eq("type_name",hotspotType.getTypeName()).ne("id",hotspotType.getId());
            HotspotType hotspotType1 = hotspotTypeMapper.selectOne(wrapper);
            if(!Objects.isNull(hotspotType1)){
                return new ResponseResult(202,"分类已存在");
            }
        }
        LocalDate localDate = LocalDate.now();
        hotspotType.setUpdateTime(localDate);
        int result = hotspotTypeMapper.updateById(hotspotType);
        if(result > 0){
            // 获取用户id
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            // 添加日志信息
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("修改热点分类id:"+hotspotType.getId());
            loggerService.insertLoggerInfo(logger);
            return new ResponseResult(200,"修改成功");
        }
        return new ResponseResult(201,"修改失败");
    }

    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public ResponseResult deleteHotspotType(Integer id) {
        // 删除有关信息
        hotspotInfoMapper.delete(new QueryWrapper<HotspotInfo>().eq("type_id",id));
        // 删除
        int i = hotspotTypeMapper.deleteById(id);
        if(i > 0){
            // 添加日志信息
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("删除了分类id: "+ id);
            return new ResponseResult(200,"删除成功");
        }
        return new ResponseResult(201,"删除失败");
    }
    /**
     * 发布/下架
     * @param id
     * @return
     */
    @Override
    public ResponseResult updateHotspotTypeInStatus(Integer id) {
        // 设置初始值status,msg
        Integer status = 0;
        String msg = "发布";
        // 判断当前状态
        HotspotType hotspotType = hotspotTypeMapper.selectById(id);
        Integer s = hotspotType.getStatus();
        // 取反
        if(s == 0){
            status = 1;
            msg = "下架";
        }
        // 修改当前状态
        UpdateWrapper<HotspotType> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",id).set("status",status);
        int result = hotspotTypeMapper.update(null, wrapper);
        if(result > 0){
            // 获取用户id
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            // 添加日志信息
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode(msg+"了诈骗模块id："+id);
            loggerService.insertLoggerInfo(logger);
            return new ResponseResult(200,msg+"成功");
        }
        return new ResponseResult(201,msg+"失败");
    }
}




