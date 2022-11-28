package com.rural.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.Data;
import lombok.ToString;

/**
 * 
 * @TableName wx_user
 */
@TableName(value ="wx_user")
@Data
@ToString
public class WxUser implements Serializable {

    @TableId
    private String openId;

    /**
     * session_key
     */
    private String sessionKey;

    /**
     * 头像路径
     */
    private String avatarUrl;

    /**
     * 城市
     */
    private String city;

    /**
     * 性别
     */
    private String gender;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 国家
     */
    private String country;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 创建日期
     */
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}