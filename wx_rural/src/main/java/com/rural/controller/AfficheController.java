package com.rural.controller;

import com.rural.pojo.Affiche;
import com.rural.pojo.ResponseResult;
import com.rural.service.AfficheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @author xh
 * @create 2022-11-20  15:55
 */
@RestController
@RequestMapping("/affiche")
public class AfficheController {

    @Autowired
    private AfficheService afficheService;

    /**
     * 添加公告
     * @param affiche
     * @return ResponseResult(code,msg)
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult insertAffiche(@RequestBody Affiche affiche){
        return afficheService.insertAffiche(affiche);
    }

    /**
     * 删除公告
     * @param id
     * @return ResponseResult(code,msg)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult deleteAffiche(@PathVariable("id") Integer id){
        return afficheService.deleteAffiche(id);
    }

    /**
     * 修改公告
     * @param affiche
     * @return 返回ResponseResult(code,msg)
     */
    @PutMapping
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult updateAffiche(@RequestBody Affiche affiche){
        return afficheService.updateAffiche(affiche);
    }
    /**
     * 发布公告
     * @param id
     * @return 返回ResponseResult(code,msg)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('root','admin')")
    public ResponseResult pubAffiche(@PathVariable("id") Integer id){
        return afficheService.pubAffiche(id);
    }

    /**
     * 客服端查询公告主内容
     * @param id
     * @return 返回ResponseResult(code,msg,data)
     */
    @GetMapping("/{id}")
    public ResponseResult selectAfficheById(@PathVariable("id") Integer id){
        return afficheService.selectAfficheById(id);
    }

    /**
     * 客服端公告显示
     * @return 返回ResponseResult(code,msg,data)
     */
    @GetMapping("/wx")
    public ResponseResult selectAfficheByStatus(){
        return afficheService.selectAfficheByStatus();
    }

    /**
     * 后台查询所有公告
     * @param current
     * @param size
     * @param msg
     * @param type
     * @return 返回ResponseResult(code,msg，data)
     */
    @GetMapping("/{size}/{current}")
    @PreAuthorize("hasAnyAuthority('admin','root')")
    public ResponseResult selectAllAffiche(@PathVariable("size") Integer size,
                                           @PathVariable("current") Integer current,
                                           @RequestParam String msg,
                                           @RequestParam String type){
        return afficheService.selectAllAffiche(size,current,msg,type);
    }





}
