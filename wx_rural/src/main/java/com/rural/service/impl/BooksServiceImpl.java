package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.mapper.BooksNoteMapper;
import com.rural.pojo.*;
import com.rural.service.BooksChapterService;
import com.rural.service.BooksNoteService;
import com.rural.service.BooksService;
import com.rural.mapper.BooksMapper;
import com.rural.service.LoggerService;
import com.rural.utils.PageInfoUtils;
import com.rural.utils.SecurityContextHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 *
 */
@Service
public class BooksServiceImpl extends ServiceImpl<BooksMapper, Books>
    implements BooksService{
    private final static String TYPE_NAME = "全民普法";
    @Autowired
    private BooksMapper booksMapper;
    @Autowired
    private BooksChapterService booksChapterService;
    @Autowired
    private BooksNoteService booksNoteService;
    @Autowired
    private LoggerService loggerService;

    /**
     * 添加书籍名
     * @param books
     * @return
     */
    @Override
    public ResponseResult insertBooks(Books books) {
        // 判断书名是否存在
        Books title = booksMapper.selectOne(new QueryWrapper<Books>().eq("title", books.getTitle()));
        if(!Objects.isNull(title)){
            return new ResponseResult(202,"标题已存在");
        }
        // 创建时间
        LocalDate localDate = LocalDate.now();
        books.setCreateTime(localDate);
        books.setUpdateTime(localDate);
        // 添加
        int result = booksMapper.insert(books);
        if(result>0){
            // 获取用户id
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            // 添加日志信息
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("添加了书籍名称: "+books.getTitle());
            loggerService.insertLoggerInfo(logger);
            //返回
            return new ResponseResult(200,"添加成功");
        }
        return new ResponseResult(201,"添加失败");
    }

    /**
     * 删除书籍
     * @param id
     * @return
     */
    @Override
    public ResponseResult deleteBooks(Integer id) {
        // 获取章节所有id
        List<BooksChapter> booksChapters = booksChapterService.list(new QueryWrapper<BooksChapter>().eq("books_id", id));
        // 创建链表存储章节id
        List<Integer> ids = booksChapters.stream()
                .map(BooksChapter::getId)
                .collect(Collectors.toList());
        // 批量删除
        if(ids.size() > 0) {
            booksNoteService.deleteBatchByChapterId(ids);
        }
        // 删除相关章节
        booksChapterService.remove(new QueryWrapper<BooksChapter>().eq("books_id",id));
        // 删除书籍
        int result = booksMapper.deleteById(id);
        if(result > 0){
            // 获取用户id
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            // 添加日志信息
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("删除了书籍id: "+id);
            loggerService.insertLoggerInfo(logger);
            //返回
            return new ResponseResult(200,"删除成功");
        }
        return new ResponseResult(200,"删除失败");
    }

    /**
     * 修改书籍
     * @param books
     * @return
     */
    @Override
    public ResponseResult updateBooks(Books books) {
        // 判断title是否为空
        if(StringUtils.hasText(books.getTitle())){
            // 判断是否存在
            QueryWrapper<Books> wrapper = new QueryWrapper<>();
            wrapper.eq("title",books.getTitle()).ne("id",books.getId());
            Books books1 = booksMapper.selectOne(wrapper);
            if(!Objects.isNull(books1)){
                return new ResponseResult(202,"标题已存在");
            }
        }
        LocalDate localDate = LocalDate.now();
        books.setUpdateTime(localDate);
        int result = booksMapper.updateById(books);
        if(result > 0){
            // 获取用户id
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            // 添加日志信息
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("修改了书籍id:"+books.getId());
            loggerService.insertLoggerInfo(logger);
            return new ResponseResult(200,"修改成功");
        }
        return new ResponseResult(201,"修改失败");

    }

    /**
     * 发布/下架书籍
     * @param id
     * @return
     */
    @Override
    public ResponseResult updateBooksInStatus(Integer id) {
        // 设置初始值status,msg
        Integer status = 0;
        String msg = "发布";
        // 判断当前状态
        Books books = booksMapper.selectById(id);
        Integer s = books.getStatus();
        // 取反
        if(s == 0){
            status = 1;
            msg = "下架";
        }
        // 修改当前状态
        UpdateWrapper<Books> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",id).set("status",status);
        int result = booksMapper.update(null, wrapper);
        if(result > 0){
            // 获取用户id
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            // 添加日志信息
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode(msg+"了书籍名称："+books.getTitle());
            loggerService.insertLoggerInfo(logger);
            return new ResponseResult(200,msg+"成功");
        }
        return new ResponseResult(201,msg+"失败");
    }
    /**
     * 微信端查询列表
     * @return
     */
    @Override
    public ResponseResult selectBooksWx() {
        // 直接查询
        try {
            List<Books> status = booksMapper.selectList(new QueryWrapper<Books>().eq("status", 0));
            return new ResponseResult(200,"查询成功",status);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }
    }
    /**
     * 后台查询列表
     * @param size
     * @param current
     * @param msg
     * @param type
     * @return
     */
    @Override
    public ResponseResult selectBooks(Integer size, Integer current, String msg, String type) {
        Page<Books> page = new Page<>(current,size);
        Page<Books> booksPage = null;
        // 判断msg是否为空
        if(StringUtils.hasText(msg)){
            //判断type是否为空
            if(!StringUtils.hasText(type)){
                // 默认值
                type = "title";
            }
            booksPage = booksMapper.selectPage(page, new QueryWrapper<Books>().like(type,msg));
        }else {
            booksPage = booksMapper.selectPage(page, null);
        }
        // 判断是否为空
        if(Objects.isNull(booksPage)){
            return new ResponseResult(201,"查询失败");
        }
        List<Books> booksList = booksPage.getRecords();
        Map<String,Object> maps = new HashMap<>();
        maps.put("info",booksList);
        Map<String, Object> pageInfo = PageInfoUtils.getPageInfo(booksPage);
        maps.put("pageInfo",pageInfo);
        return new ResponseResult(200,"查询成功",maps);
    }
}




