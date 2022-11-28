package com.rural.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rural.pojo.TelephoneBook;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.rural.pojo.TelephoneBook
 */
@Repository
public interface TelephoneBookMapper extends BaseMapper<TelephoneBook> {

    Page<TelephoneBook> selectAll(@Param("page") Page page);

    List<TelephoneBook> selectAllById(Integer id);
}




