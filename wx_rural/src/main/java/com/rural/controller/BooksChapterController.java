package com.rural.controller;

import com.rural.pojo.BooksChapter;
import com.rural.pojo.ResponseResult;
import com.rural.service.BooksChapterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author xh
 * @create 2022-11-28  15:47
 */
@RestController
@RequestMapping("/books/chapter")
public class BooksChapterController {

    @Autowired
    private BooksChapterService booksChapterService;

    /**
     * 添加章节
     * @param booksChapter 实体类对象
     * @return ResponseResult(code,msg)
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult insertBooksChapter(@RequestBody BooksChapter booksChapter){
        return booksChapterService.insertBooksChapter(booksChapter);
    }

    /**
     * 删除章节
     * @param id 章节id
     * @return ResponseResult(code,msg)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult deleteBooksChapter(@PathVariable("id") Integer id){
        return booksChapterService.deleteBooksChapter(id);
    }

    /**
     * 修改章节
     * @param booksChapter 实体类对象
     * @return ResponseResult(code,msg)
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updateBooksChapter(@RequestBody BooksChapter booksChapter){
        return booksChapterService.updateBooksChapter(booksChapter);
    }

    // 查询

    /**
     * 微信端显示列表
     * @param size 每页记录数
     * @param current 当前页码
     * @param id 书籍id
     * @return ResponseResult(code,msg,data)
     */
    @GetMapping("/wx/{size}/{current}/{id}")
    public ResponseResult selectBooksChapterWx(@PathVariable("size") Integer size,
                                               @PathVariable("current") Integer current,
                                               @PathVariable("id") Integer id){
        return booksChapterService.selectBooksChapterWx(size,current,id);
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
    @GetMapping("/{size}/{current}/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult selectBooksChapter(@PathVariable("size") Integer size,
                                             @PathVariable("current") Integer current,
                                             @PathVariable("id") Integer id,
                                             @RequestParam String msg,
                                             @RequestParam String type){
        return booksChapterService.selectBooksChapter(size,current,id,msg,type);
    }
}
