package com.rural.mapper;

import com.rural.pojo.BooksNote;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.rural.pojo.BooksNote
 */
@Repository
public interface BooksNoteMapper extends BaseMapper<BooksNote> {

    int deleteBatchByChapterId(@Param("ids") List<Integer> ids);
}




