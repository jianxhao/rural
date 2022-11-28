package com.rural.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xh
 * @create 2022-11-19  16:10
 */
public class PageInfoUtils {

    public static Map<String,Object> getPageInfo(Page page){
        Map<String,Object> mapPage = new HashMap<>();
        mapPage.put("current",page.getCurrent());
        mapPage.put("size",page.getSize());
        mapPage.put("total",page.getTotal());
        mapPage.put("hasPrevious",page.hasPrevious());
        mapPage.put("hasNext",page.hasNext());
        return mapPage;
    }
}
