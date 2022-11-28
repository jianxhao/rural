package com.rural.mapper;

import com.rural.pojo.AdminUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.rural.pojo.AdminUser
 */
@Repository
public interface AdminUserMapper extends BaseMapper<AdminUser> {

    void addPermission(@Param("id") Integer id, @Param("roleId") Integer roleId);
}




