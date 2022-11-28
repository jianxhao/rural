package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.pojo.Logger;
import com.rural.pojo.ResponseResult;
import com.rural.pojo.StaticPage;
import com.rural.service.LoggerService;
import com.rural.service.StaticPageService;
import com.rural.mapper.StaticPageMapper;
import com.rural.utils.SecurityContextHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 *
 */
@Service
public class StaticPageServiceImpl extends ServiceImpl<StaticPageMapper, StaticPage>
    implements StaticPageService{

    private final static String TYPE_NAME = "静态页面";

    @Autowired
    private StaticPageMapper staticPageMapper;
    @Autowired
    private LoggerService loggerService;

    /**
     * 根据页面查询
     * @param page
     * @return ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectByPage(String page) {

        // 根据page_name 查询
        QueryWrapper<StaticPage> wrapper = new QueryWrapper<>();
        wrapper.eq("page_name",page);
        try {
            StaticPage staticPage = staticPageMapper.selectOne(wrapper);
            return new ResponseResult(200,"查询成功",staticPage);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }
    }
    /**
     * 查询所有静态页面
     * @return ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectAllStaticPage() {
        try {
            List<StaticPage> staticPages = staticPageMapper.selectList(null);
            return new ResponseResult(200,"查询成功",staticPages);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }
    }

    /**
     * 添加静态页面
     * @param staticPage
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult insertStaticPage(StaticPage staticPage) {
        // 判断页面是否存在
        StaticPage page_name = staticPageMapper.selectOne(new QueryWrapper<StaticPage>().eq("page_name", staticPage.getPageName()));
        if(!Objects.isNull(page_name)){
            return new ResponseResult(202,"页面已存在");
        }
        int result = staticPageMapper.insert(staticPage);
        if(result > 0){
            // 添加日志信息
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("添加了"+staticPage.getPageName()+"页面静态资源");
            loggerService.insertLoggerInfo(logger);
            return new ResponseResult(200,"添加成功");
        }
        return new ResponseResult(201,"添加失败");
    }

    /**
     * 删除静态页面
     * @param id
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult deleteStaticPage(Integer id) {
        StaticPage staticPage = staticPageMapper.selectById(id);
        int result = staticPageMapper.deleteById(id);
        if(result > 0){
            // 添加日志信息
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("删除了"+staticPage.getPageName()+"页面静态资源");
            loggerService.insertLoggerInfo(logger);
            return new ResponseResult(200,"删除成功");
        }
        return new ResponseResult(201,"删除失败");
    }

    /**
     * 修改静态页面
     * @param staticPage
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult updateStaticPage(StaticPage staticPage) {
        StaticPage staticPage1 = staticPageMapper.selectById(staticPage.getId());
        // 判断pageName是否为空
        if(!Objects.isNull(staticPage.getPageName()) && !staticPage.getPageName().equals(staticPage1.getPageName())){
            StaticPage pageName = staticPageMapper.selectOne(new QueryWrapper<StaticPage>().eq("page_name", staticPage.getPageName()));
            if(!Objects.isNull(pageName)){
                return new ResponseResult(202,"页面已存在");
            }
        }
        // 修改
        int result = staticPageMapper.updateById(staticPage);
        if(result>0){
            // 添加日志信息
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("修改了"+staticPage.getPageName()+"页面静态资源");
            loggerService.insertLoggerInfo(logger);
            return new ResponseResult(200,"添加成功");
        }
        return new ResponseResult(201,"修改失败");
    }
}




