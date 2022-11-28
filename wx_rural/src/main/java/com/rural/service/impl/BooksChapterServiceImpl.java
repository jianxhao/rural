package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.pojo.BooksChapter;
import com.rural.pojo.BooksNote;
import com.rural.pojo.Logger;
import com.rural.pojo.ResponseResult;
import com.rural.service.BooksChapterService;
import com.rural.mapper.BooksChapterMapper;
import com.rural.service.BooksNoteService;
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
public class BooksChapterServiceImpl extends ServiceImpl<BooksChapterMapper, BooksChapter>
    implements BooksChapterService{
    private final static String TYPE_NAME = "书籍章节";
    @Autowired
    private BooksChapterMapper booksChapterMapper;
    @Autowired
    private BooksNoteService booksNoteService;
    @Autowired
    private LoggerService loggerService;

    /**
     * 添加章节
     * @param booksChapter 实体类对象
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult insertBooksChapter(BooksChapter booksChapter) {
        // 创建时间
        LocalDate localDate = LocalDate.now();
        booksChapter.setCreateTime(localDate);
        booksChapter.setUpdateTime(localDate);
        // 添加章节
        int result = booksChapterMapper.insert(booksChapter);
        if (result > 0){
            // 获取用户id
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            // 添加日志信息
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("添加了书籍章节名称: "+booksChapter.getTitle());
            loggerService.insertLoggerInfo(logger);
            //返回
            return new ResponseResult(200,"添加成功");
        }
        return new ResponseResult(201,"添加失败");
    }

    /**
     * 删除章节
     * @param id 章节id
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult deleteBooksChapter(Integer id) {
        // 删除相关信息
        booksNoteService.remove(new QueryWrapper<BooksNote>().eq("chapter_id",id));
        // 删除章节
        int result = booksChapterMapper.deleteById(id);
        if(result > 0){
            // 获取用户id
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            // 添加日志信息
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("删除了书籍章节id: "+id);
            loggerService.insertLoggerInfo(logger);
            //返回
            return new ResponseResult(200,"删除成功");
        }
        return new ResponseResult(201,"删除失败");
    }

    /**
     * 修改章节
     * @param booksChapter 实体类对象
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult updateBooksChapter(BooksChapter booksChapter) {
        // 更新时间
        LocalDate localDate = LocalDate.now();
        booksChapter.setUpdateTime(localDate);
        // 修改
        int result = booksChapterMapper.updateById(booksChapter);
        if(result > 0){
            // 获取用户id
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            // 添加日志信息
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("把书籍章节id:"+booksChapter.getId()+",修改章节名称为: "+booksChapter.getTitle());
            loggerService.insertLoggerInfo(logger);
            //返回
            return new ResponseResult(200,"修改成功");
        }
        return new ResponseResult(201,"修改失败");
    }

    /**
     * 微信端显示列表
     * @param size 每页记录数
     * @param current 当前页码
     * @param id 书籍id
     * @return ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectBooksChapterWx(Integer size, Integer current, Integer id) {
        // 创建分页
        Page<BooksChapter> page = new Page<>(current,size);
        // 查询
        QueryWrapper<BooksChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("books_id",id);
        Page<BooksChapter> booksChapterPage = booksChapterMapper.selectPage(page, wrapper);
        // 判断是否为空
        if(Objects.isNull(booksChapterPage)){
            return new ResponseResult(201,"查询失败");
        }
        List<BooksChapter> booksChapterList = booksChapterPage.getRecords();
        Map<String,Object> maps = new HashMap<>();
        maps.put("info",booksChapterList);
        Map<String, Object> pageInfo = PageInfoUtils.getPageInfo(booksChapterPage);
        maps.put("pageInfo",pageInfo);
        return new ResponseResult(200,"查询成功",maps);
    }

    /**
     * 后台查询列表
     * @param size 每页记录数
     * @param current 当前页码
     * @param id 书籍id
     * @param msg 查询内容
     * @param type 条件
     * @return ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectBooksChapter(Integer size, Integer current, Integer id, String msg, String type) {
        Page<BooksChapter> page = new Page<>(current,size);
        Page<BooksChapter> booksChapterPage = null;
        // 判断msg是否为空
        if(StringUtils.hasText(msg)){
            //判断type是否为空
            if(!StringUtils.hasText(type)){
                // 默认值
                type = "title";
            }
            booksChapterPage = booksChapterMapper.selectPage(page, new QueryWrapper<BooksChapter>().eq("books_id",id).like(type,msg));
        }else {
            booksChapterPage = booksChapterMapper.selectPage(page, new QueryWrapper<BooksChapter>().eq("books_id",id));
        }
        // 判断是否为空
        if(Objects.isNull(booksChapterPage)){
            return new ResponseResult(201,"查询失败");
        }
        List<BooksChapter> booksChapterList = booksChapterPage.getRecords();
        Map<String,Object> maps = new HashMap<>();
        maps.put("info",booksChapterList);
        Map<String, Object> pageInfo = PageInfoUtils.getPageInfo(booksChapterPage);
        maps.put("pageInfo",pageInfo);
        return new ResponseResult(200,"查询成功",maps);
    }
}




