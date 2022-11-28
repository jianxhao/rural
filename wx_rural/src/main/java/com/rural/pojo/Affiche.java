package com.rural.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName affiche
 */
@TableName(value ="affiche")
@Data
public class Affiche implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 简介
     */
    private String blurb;

    /**
     * 描述
     */
    private String note;

    /**
     * 创建时间
     */
    private LocalDate createTime;

    /**
     * 0公示中,1过期
     */
    private Integer status;

    /**
     * 更新时间
     */
    private LocalDate updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}