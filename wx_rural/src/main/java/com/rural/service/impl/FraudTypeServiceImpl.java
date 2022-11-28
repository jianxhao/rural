package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.mapper.FraudInfoMapper;
import com.rural.pojo.*;
import com.rural.service.FraudTypeService;
import com.rural.mapper.FraudTypeMapper;
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
public class FraudTypeServiceImpl extends ServiceImpl<FraudTypeMapper, FraudType>
    implements FraudTypeService{
    private final static String TYPE_NAME = "诈骗分类";
    @Autowired
    private FraudTypeMapper fraudTypeMapper;
    @Autowired
    private LoggerService loggerService;
    @Autowired
    private FraudInfoMapper fraudInfoMapper;

    /**
     * 查询
     * @return
     */
    @Override
    public ResponseResult selectAllFraudType() {
        // 查询所有
        try {
            List<FraudType> fraudTypes = fraudTypeMapper.selectList(null);
            return new ResponseResult(200,"查询成功",fraudTypes);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }
    }

    /**
     * 添加
     * @param fraudType
     * @return
     */
    @Override
    public ResponseResult insertFraudType(FraudType fraudType) {
        // 判断模块是否存在
        QueryWrapper<FraudType> wrapper = new QueryWrapper<>();
        wrapper.eq("type_name",fraudType.getTypeName());
        FraudType fraudType1 = fraudTypeMapper.selectOne(wrapper);
        if(!Objects.isNull(fraudType1)){
            return new ResponseResult(202,"分类已存在");
        }
        // 添加
        LocalDate localDate = LocalDate.now();
        fraudType.setCreateTime(localDate);
        fraudType.setUpdateTime(localDate);
        int result = fraudTypeMapper.insert(fraudType);
        if(result > 0 ){
            // 添加日志信息
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("添加了分类名: "+ fraudType.getTypeName());
            loggerService.insertLoggerInfo(logger);
            return new ResponseResult(200,"添加成功");
        }
        return new ResponseResult(201,"添加失败");
    }
    /**
     * 修改
     * @param fraudType
     * @return
     */
    @Override
    public ResponseResult updateFraudType(FraudType fraudType) {
        // 判断typeName是否为空
        if(StringUtils.hasText(fraudType.getTypeName())){
            // 判断是否存在
            QueryWrapper<FraudType> wrapper = new QueryWrapper<>();
            wrapper.eq("type_name",fraudType.getTypeName()).ne("id",fraudType.getId());
            FraudType fraudType1 = fraudTypeMapper.selectOne(wrapper);
            if(!Objects.isNull(fraudType1)){
                return new ResponseResult(202,"分类已存在");
            }
        }
        LocalDate localDate = LocalDate.now();
        fraudType.setUpdateTime(localDate);
        int result = fraudTypeMapper.updateById(fraudType);
        if(result > 0){
            // 获取用户id
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            // 添加日志信息
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("修改诈骗分类id:"+fraudType.getId());
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
    public ResponseResult deleteFraudType(Integer id) {
        // 删除有关信息
        fraudInfoMapper.delete(new QueryWrapper<FraudInfo>().eq("type_id",id));
        // 删除
        int i = fraudTypeMapper.deleteById(id);
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
     * 查询(微信)
     * @return
     */
    @Override
    public ResponseResult selectAllFraudTypeWx() {
        // 查询所有
        try {
            List<FraudType> fraudTypes = fraudTypeMapper.selectList(new QueryWrapper<FraudType>().eq("status",0));
            return new ResponseResult(200,"查询成功",fraudTypes);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }
    }
    /**
     * 发布/下架
     * @param id
     * @return
     */
    @Override
    public ResponseResult updateFraudTypeInStatus(Integer id) {
        // 设置初始值status,msg
        Integer status = 0;
        String msg = "发布";
        // 判断当前状态
        FraudType fraudType = fraudTypeMapper.selectById(id);
        Integer s = fraudType.getStatus();
        // 取反
        if(s == 0){
            status = 1;
            msg = "下架";
        }
        // 修改当前状态
        UpdateWrapper<FraudType> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",id).set("status",status);
        int result = fraudTypeMapper.update(null, wrapper);
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




