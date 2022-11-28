package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.pojo.*;
import com.rural.service.AfficheService;
import com.rural.mapper.AfficheMapper;
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
public class AfficheServiceImpl extends ServiceImpl<AfficheMapper, Affiche>
    implements AfficheService{

    private final static String TYPE_NAME = "公告栏";

    @Autowired
    private LoggerService loggerService;

    @Autowired
    private AfficheMapper afficheMapper;

    /**
     * 添加公告
     * @param affiche
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult insertAffiche(Affiche affiche) {

        // 创建时间
        LocalDate localDate = LocalDate.now();
        // 添加时，创建时间和更新时间一致
        affiche.setCreateTime(localDate);
        affiche.setUpdateTime(localDate);

        try {
            int result = afficheMapper.insert(affiche);
            if(result > 0){
                // 添加日志信息
                Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
                Logger logger = new Logger();
                logger.setUserId(userId);
                logger.setModule(TYPE_NAME);
                logger.setNode("添加公告id: "+ affiche.getId());
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
     * 删除公告
     * @param id
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult deleteAffiche(Integer id) {
        try {
            int result = afficheMapper.deleteById(id);
            if(result > 0){
                // 添加日志信息
                Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
                Logger logger = new Logger();
                logger.setUserId(userId);
                logger.setModule(TYPE_NAME);
                logger.setNode("删除公告id: "+ id);
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
     * 修改公告
     * @return 返回ResponseResult(code,msg)
     */
    @Override
    public ResponseResult updateAffiche(Affiche affiche) {
        LocalDate localDate = LocalDate.now();
        affiche.setUpdateTime(localDate);
        try {
            // 执行修改操作
            int result = afficheMapper.updateById(affiche);
            if(result > 0){
                // 添加日志信息
                Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
                Logger logger = new Logger();
                logger.setUserId(userId);
                logger.setModule(TYPE_NAME);
                logger.setNode("修改公告ID: "+affiche.getId());
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
     * 查询公告主内容
     * @return 返回ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult  selectAfficheById(Integer id) {
        try {
            Affiche affiche = afficheMapper.selectById(id);
            Map<String,Object> map = new HashMap<>();
            if(!Objects.isNull(affiche)) {
                map.put("context",affiche.getNote());
                return new ResponseResult(200, "查询成功", map);
            }
            return new ResponseResult(201, "查询失败");
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }
    }

    /**
     * 查询所有公告
     * @return 返回ResponseResult(code,msg，data)
     */
    @Override
    public ResponseResult selectAllAffiche(Integer size,Integer current,String msg,String type) {
        Page<Affiche> page = new Page<>(current,size);
        Page<Affiche> affichePage = null;
        // msg为空时查找所有
        if(!StringUtils.hasText(msg)){
            affichePage = afficheMapper.selectPage(page, null);
        }else{
            if(!StringUtils.hasText(type)){
                // 默认值
                type = "blurb";
            }
            // 模糊查找
            QueryWrapper<Affiche> wrapper = new QueryWrapper<>();
            wrapper.like(type,msg);
            affichePage = afficheMapper.selectPage(page, wrapper);
        }
        if(Objects.isNull(affichePage)){
            return new ResponseResult(201,"查询失败");
        }
        List<Affiche> records = affichePage.getRecords();
        // 返回数据
        Map<String,Object> maps = new HashMap<>();
        maps.put("info",records);
        Map<String, Object> pageInfo1 = PageInfoUtils.getPageInfo(affichePage);
        maps.put("pageInfo",pageInfo1);
        return new ResponseResult(200,"查询成功",maps);
    }

    /**
     * 客服端公告显示
     * @return 返回ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectAfficheByStatus() {
        try {
            QueryWrapper<Affiche> wrapper = new QueryWrapper<>();
            wrapper.eq("status",0);
            Affiche affiche = afficheMapper.selectOne(wrapper);
            return new ResponseResult(200, "查询成功", affiche);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }
    }
    /**
     * 发布公告
     * @return 返回ResponseResult(code,msg)
     */
    @Override
    public ResponseResult pubAffiche(Integer id) {
        Integer s = 0;
        String msg = "发布成功";
        Affiche affiche = afficheMapper.selectById(id);
        if(affiche.getStatus() == 0){
             s = 1;
             msg = "下架成功";
        }
        // 1.把其他公告状态设置为1
        UpdateWrapper<Affiche> updateWrapper = new UpdateWrapper<>();
        updateWrapper.ne("id",id)
                .set("status",1);
        afficheMapper.update(null,updateWrapper);
        // 2.清空updateWrapper.clear();
        updateWrapper.clear();
        // 3.把当前id状态设置为0，表示公示
        updateWrapper.eq("id",id)
                .set("status",s);
        int result = afficheMapper.update(null, updateWrapper);
        if(result > 0){
            // 添加日志信息
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("发布公告ID: "+id);
            loggerService.insertLoggerInfo(logger);
            return new ResponseResult(200,msg);
        }else{
            return new ResponseResult(201,"发布失败");
        }

    }


}




