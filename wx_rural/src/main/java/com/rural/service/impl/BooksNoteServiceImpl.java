package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.pojo.BooksNote;
import com.rural.pojo.Logger;
import com.rural.pojo.ResponseResult;
import com.rural.service.BooksNoteService;
import com.rural.mapper.BooksNoteMapper;
import com.rural.service.LoggerService;
import com.rural.utils.SecurityContextHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 *
 */
@Service
public class BooksNoteServiceImpl extends ServiceImpl<BooksNoteMapper, BooksNote>
    implements BooksNoteService{
    private final static String TYPE_NAME = "章节内容";
    @Autowired
    private BooksNoteMapper booksNoteMapper;
    @Autowired
    private LoggerService loggerService;
    /**
     * 添加章节内容
     * @param booksNote 实体类对象
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult insertBooksNote(BooksNote booksNote) {
        //创建时间
        LocalDate localDate = LocalDate.now();
        booksNote.setCreateTime(localDate);
        booksNote.setUpdateTime(localDate);
        // 添加
        int result = booksNoteMapper.insert(booksNote);
        if (result > 0){
            // 获取用户id
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            // 添加日志信息
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("添加了章节id: "+booksNote.getChapterId()+"的内容");
            loggerService.insertLoggerInfo(logger);
            //返回
            return new ResponseResult(200,"添加成功");
        }
        return new ResponseResult(201,"添加失败");
    }
    /**
     * 删除章节内容
     * @param id 内容id
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult deleteBooksNote(Integer id) {
        // 删除
        int result = booksNoteMapper.deleteById(id);
        if(result > 0){
            // 获取用户id
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            // 添加日志信息
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("删除了章节内容id: "+id);
            loggerService.insertLoggerInfo(logger);
            //返回
            return new ResponseResult(200,"删除成功");
        }
        return new ResponseResult(201,"删除失败");
    }
    /**
     * 修改章节内容
     * @param booksNote 实体类对象
     * @return ResponseResult(code,msg)
     */
    @Override
    public ResponseResult updateBooksNote(BooksNote booksNote) {
        // 更新时间
        LocalDate localDate = LocalDate.now();
        booksNote.setUpdateTime(localDate);
        // 修改
        int result = booksNoteMapper.updateById(booksNote);
        if(result > 0){
            // 获取用户id
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            // 添加日志信息
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("修改了章节内容");
            loggerService.insertLoggerInfo(logger);
            //返回
            return new ResponseResult(200,"修改成功");
        }
        return new ResponseResult(201,"修改失败");
    }
    /**
     * 查询章节内容
     * @param id 章节id
     * @return ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectBooksNote(Integer id) {
        // 查询
        try {
            List<BooksNote> notes = booksNoteMapper.selectList(new QueryWrapper<BooksNote>().eq("chapter_id", id));
            return new ResponseResult(200,"查询成功",notes);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }

    }

    @Override
    public int deleteBatchByChapterId(List<Integer> ids) {
        return booksNoteMapper.deleteBatchByChapterId(ids);
    }
}




