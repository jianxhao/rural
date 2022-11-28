package com.rural.controller;

import com.rural.pojo.BooksNote;
import com.rural.pojo.ResponseResult;
import com.rural.service.BooksNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author xh
 * @create 2022-11-28  16:28
 */
@RestController
@RequestMapping("/books/note")
public class BooksNoteController {
    @Autowired
    private BooksNoteService booksNoteService;

    /**
     * 添加章节内容
     * @param booksNote 实体类对象
     * @return ResponseResult(code,msg)
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult insertBooksNote(@RequestBody BooksNote booksNote){
        return booksNoteService.insertBooksNote(booksNote);
    }

    /**
     * 删除章节内容
     * @param id 内容id
     * @return ResponseResult(code,msg)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult deleteBooksNote(@PathVariable("id") Integer id){
        return booksNoteService.deleteBooksNote(id);
    }

    /**
     * 修改章节内容
     * @param booksNote 实体类对象
     * @return ResponseResult(code,msg)
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updateBooksNote(@RequestBody BooksNote booksNote){
        return booksNoteService.updateBooksNote(booksNote);
    }
    // 查询

    /**
     * 查询章节内容
     * @param id 章节id
     * @return ResponseResult(code,msg,data)
     */
    @GetMapping("/{id}")
    public ResponseResult selectBooksNote(@PathVariable("id") Integer id){
        return booksNoteService.selectBooksNote(id);
    }

}
