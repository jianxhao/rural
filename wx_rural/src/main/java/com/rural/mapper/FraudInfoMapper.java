package com.rural.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rural.pojo.FraudInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.rural.pojo.FraudInfo
 */
@Repository
public interface FraudInfoMapper extends BaseMapper<FraudInfo> {

    Page<FraudInfo> selectPageAll(@Param("page") Page<FraudInfo> page, @Param("type") String type, @Param("msg") String msg);

    Page<FraudInfo> selectListById(@Param("param") Page<FraudInfo> page, @Param("id") Integer id);
}




