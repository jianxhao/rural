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
 * @TableName telephone_book
 */
@TableName(value ="telephone_book")
@Data
public class TelephoneBook implements Serializable {
    /**
     * 电话本id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 电话号
     */
    private String phone;

    /**
     * 描述
     */
    private String note;

    /**
     * 创建日期
     */
    private LocalDate createTime;

    /**
     * 电话分类id
     */
    private Integer typeId;

    /**
     * 电话分类名称
     */
    @TableField(exist = false)
    private String typeName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}