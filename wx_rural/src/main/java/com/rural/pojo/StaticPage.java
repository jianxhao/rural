package com.rural.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName static_page
 */
@TableName(value ="static_page")
@Data
public class StaticPage implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 页面名称
     */
    private String pageName;

    /**
     * 轮播图1
     */
    private String carouselOne;

    /**
     * 轮播图2
     */
    private String carouselTwo;

    /**
     * 轮播图3
     */
    private String carouselThree;

    /**
     * 展示图
     */
    private String imageUrl;

    /**
     * 其他图
     */
    private String otherUrl;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}