package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.mapper.TelephoneBookMapper;
import com.rural.pojo.*;
import com.rural.service.LoggerService;
import com.rural.service.PhoneTypeService;
import com.rural.mapper.PhoneTypeMapper;
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
public class PhoneTypeServiceImpl extends ServiceImpl<PhoneTypeMapper, PhoneType>
    implements PhoneTypeService{
    private final static String TYPE_NAME = "热线通分类";
    @Autowired
    private PhoneTypeMapper phoneTypeMapper;
    @Autowired
    private LoggerService loggerService;
    @Autowired
    private TelephoneBookMapper telephoneBookMapper;

    /**
     * 查询
     * @return
     */
    @Override
    public ResponseResult selectAllPhoneType() {
        // 查询所有
        try {
            List<PhoneType> phoneTypes = phoneTypeMapper.selectList(null);
            return new ResponseResult(200,"查询成功",phoneTypes);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }
    }

    /**
     * 添加
     * @param phoneType
     * @return
     */
    @Override
    public ResponseResult insertPhoneType(PhoneType phoneType) {
        // 判断模块是否存在
        QueryWrapper<PhoneType> wrapper = new QueryWrapper<>();
        wrapper.eq("type_name",phoneType.getTypeName());
        PhoneType phoneType1 = phoneTypeMapper.selectOne(wrapper);
        if(!Objects.isNull(phoneType1)){
            return new ResponseResult(202,"分类已存在");
        }
        // 添加
        LocalDate localDate = LocalDate.now();
        phoneType.setCreateTime(localDate);
        phoneType.setUpdateTime(localDate);
        int result = phoneTypeMapper.insert(phoneType);
        if(result > 0 ){
            // 添加日志信息
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("添加了分类名: "+ phoneType.getTypeName());
            loggerService.insertLoggerInfo(logger);
            return new ResponseResult(200,"添加成功");
        }
        return new ResponseResult(201,"添加失败");
    }

    /**
     * 修改
     * @param phoneType
     * @return
     */
    @Override
    public ResponseResult updatePhoneType(PhoneType phoneType) {
        // 判断typeName是否为空
        if(StringUtils.hasText(phoneType.getTypeName())){
            // 判断是否存在
            QueryWrapper<PhoneType> wrapper = new QueryWrapper<>();
            wrapper.eq("type_name",phoneType.getTypeName()).ne("id",phoneType.getId());
            PhoneType phoneType1 = phoneTypeMapper.selectOne(wrapper);
            if(!Objects.isNull(phoneType1)){
                return new ResponseResult(202,"分类已存在");
            }
        }
        LocalDate localDate = LocalDate.now();
        phoneType.setUpdateTime(localDate);
        int result = phoneTypeMapper.updateById(phoneType);
        if(result > 0){
            // 获取用户id
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            // 添加日志信息
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("修改电话分类id:"+phoneType.getId());
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
    public ResponseResult deletePhoneType(Integer id) {
        // 删除有关电话
        telephoneBookMapper.delete(new QueryWrapper<TelephoneBook>().eq("type_id",id));
        // 删除
        int i = phoneTypeMapper.deleteById(id);
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
}




