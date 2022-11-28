package com.rural.controller;

import com.rural.pojo.ResponseResult;
import com.rural.pojo.StaticPage;
import com.rural.service.StaticPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author xh
 * @create 2022-11-23  18:18
 */
@RestController
@RequestMapping("/static")
public class StaticPageController {
    @Autowired
    private StaticPageService staticPageService;
    /**
     * 查询所有静态页面
     * @return ResponseResult(code,msg,data)
     */
    @GetMapping
    public ResponseResult selectAllStaticPage(){
        return staticPageService.selectAllStaticPage();
    }

    /**
     * 根据页面查询
     * @param page
     * @return ResponseResult(code,msg,data)
     */
    @GetMapping("/{page}")
    public ResponseResult selectByPage(@PathVariable("page") String page){
        return staticPageService.selectByPage(page);
    }

    /**
     * 添加静态页面
     * @param staticPage
     * @return ResponseResult(code,msg)
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult insertStaticPage(@RequestBody StaticPage staticPage){
        return staticPageService.insertStaticPage(staticPage);
    }

    /**
     * 删除静态页面
     * @param id
     * @return ResponseResult(code,msg)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult deleteStaticPage(@PathVariable("id") Integer id){
        return staticPageService.deleteStaticPage(id);
    }
    /**
     * 修改静态页面
     * @param staticPage
     * @return ResponseResult(code,msg)
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updateStaticPage(@RequestBody StaticPage staticPage){
        return staticPageService.updateStaticPage(staticPage);
    }
}
