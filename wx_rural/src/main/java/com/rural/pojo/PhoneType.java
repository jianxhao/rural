package com.rural.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;

import lombok.Data;
import org.springframework.cglib.core.Local;

/**
 * 
 * @TableName phone_type
 */
@TableName(value ="phone_type")
@Data
public class PhoneType implements Serializable {
    /**
     * 电话类型id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 电话类型名称
     */
    private String typeName;

    /**
     * 创建日期
     */
    private LocalDate createTime;

    /**
     * 更新日期
     */
    private LocalDate updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}