package com.rural.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.rural.pojo.Logger;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;

/**
 *
 */
public interface LoggerService extends IService<Logger> {

    int insertLoggerInfo(Logger logger);


    int deleteLogger(Integer id);
}
