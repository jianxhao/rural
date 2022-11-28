package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.pojo.Logger;
import com.rural.service.LoggerService;
import com.rural.mapper.LoggerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 *
 */
@Service
public class LoggerServiceImpl extends ServiceImpl<LoggerMapper, Logger>
    implements LoggerService{
    @Autowired
    private LoggerMapper loggerMapper;
    /**
     * 添加日记
     * @param logger
     * @return
     */
    @Override
    public int insertLoggerInfo(Logger logger) {
        LocalDateTime time = LocalDateTime.now();
        logger.setCreateTime(time);
        int result = loggerMapper.insert(logger);
        if(result <= 0){
            throw new RuntimeException("日记添加失败");
        }
        return result;
    }
    /**
     * 删除日记
     * @param id
     * @return
     */
    @Override
    public int deleteLogger(Integer id) {
        int result =loggerMapper.delete(new QueryWrapper<Logger>().eq("user_id",id));
        return result;
    }
}




