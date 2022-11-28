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
 * @TableName farming_info
 */
@TableName(value ="farming_info")
@Data
public class FarmingInfo implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 副标题
     */
    private String subtitle;

    /**
     * 封面链接
     */
    private String imgUrl;

    /**
     * 内容
     */
    private String note;

    /**
     * 创建日期
     */
    private LocalDate createTime;

    /**
     * 更新日期
     */
    private LocalDate updateTime;

    /**
     * 0发布中，1未发布
     */
    private Integer status;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}