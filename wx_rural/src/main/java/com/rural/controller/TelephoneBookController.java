package com.rural.controller;

import com.rural.pojo.ResponseResult;
import com.rural.pojo.TelephoneBook;
import com.rural.service.TelephoneBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author xh
 * @create 2022-11-19  9:55
 */
@RestController
@RequestMapping("/phone")
public class TelephoneBookController {

    @Autowired
    private TelephoneBookService telephoneBookService;

    /**
     * 根据typeId查询热线通全部记录
     * @return 返回ResponseResult(code,msg,data)
     */
    @GetMapping("/{id}")
    public ResponseResult selectTelephoneBookAllById(@PathVariable("id") Integer id){
        return telephoneBookService.selectByListById(id);
    }

    /**
     * 查询热线通全部记录(后台)
     * @return 返回ResponseResult(code,msg,data)
     */
    @GetMapping("/{size}/{current}")
    public ResponseResult selectTelephoneBookAll(@PathVariable("size") Integer size,
                                                 @PathVariable("current") Integer current){
        return telephoneBookService.selectByList(size,current);
    }

    /**
     * 添加热线通记录
     * @return 返回ResponseResult(code,msg,data)
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult insertTelephoneBook(@RequestBody TelephoneBook telephoneBook){
        return telephoneBookService.insertTelephoneBook(telephoneBook);
    }

    /**
     * 删除热线通记录
     * @return 返回ResponseResult(code,msg)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult deleteTelephoneBook(@PathVariable("id") Integer id){
        return telephoneBookService.deleteTelephoneBook(id);
    }

    /**
     * 修改热线通记录
     * @return 返回ResponseResult(code,msg)
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult updateTelephoneBook(@RequestBody TelephoneBook telephoneBook){
        return telephoneBookService.updateTelephoneBook(telephoneBook);
    }
}
