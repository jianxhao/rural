package com.rural.mapper;

import com.rural.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.rural.pojo.Role
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectPermsInAdmin(Integer id);

}




