package com.rural.controller;

import com.rural.pojo.Books;
import com.rural.pojo.ResponseResult;
import com.rural.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author xh
 * @create 2022-11-28  14:31
 */
@RestController
@RequestMapping("/books")
public class BooksController {

    @Autowired
    private BooksService booksService;

    /**
     * 添加书籍
     * @param books
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult insertBooks(@RequestBody Books books){
        return booksService.insertBooks(books);
    }

    /**
     * 删除书籍
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult deleteBooks(@PathVariable("id") Integer id){
        return booksService.deleteBooks(id);
    }

    /**
     * 修改书籍
     * @param books
     * @return
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updateBooks(@RequestBody Books books){
        return booksService.updateBooks(books);
    }
    /**
     * 发布/下架书籍
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updateBooksInStatus(@PathVariable("id") Integer id){
        return booksService.updateBooksInStatus(id);
    }

    // 查询

    /**
     * 微信端查询列表
     * @return
     */
    @GetMapping("/wx")
    public ResponseResult selectBooksWx(){
        return booksService.selectBooksWx();
    }

    /**
     * 后台查询列表
     * @param size
     * @param current
     * @param msg
     * @param type
     * @return
     */
    @GetMapping("/{size}/{current}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult selectBooks(@PathVariable("size") Integer size,
                                      @PathVariable("current") Integer current,
                                      @RequestParam String msg,
                                      @RequestParam String type){
        return booksService.selectBooks(size,current,msg,type);
    }
}
