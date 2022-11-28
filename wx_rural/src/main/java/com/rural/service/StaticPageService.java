package com.rural.service;

import com.rural.pojo.ResponseResult;
import com.rural.pojo.StaticPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface StaticPageService extends IService<StaticPage> {

    ResponseResult selectByPage(String page);

    ResponseResult selectAllStaticPage();

    ResponseResult insertStaticPage(StaticPage staticPage);

    ResponseResult deleteStaticPage(Integer id);

    ResponseResult updateStaticPage(StaticPage staticPage);
}
