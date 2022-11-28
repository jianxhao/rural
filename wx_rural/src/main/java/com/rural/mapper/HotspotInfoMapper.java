package com.rural.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rural.pojo.HotspotInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.rural.pojo.HotspotInfo
 */
@Repository
public interface HotspotInfoMapper extends BaseMapper<HotspotInfo> {

    Page<HotspotInfo> selectPageAll(@Param("page") Page<HotspotInfo> page, @Param("type") String type,@Param("msg") String msg);

    Page<HotspotInfo> selectListById(@Param("page") Page<HotspotInfo> page,@Param("id") Integer id);
}




