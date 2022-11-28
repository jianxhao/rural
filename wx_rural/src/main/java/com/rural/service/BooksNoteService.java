package com.rural.service;

import com.rural.pojo.BooksNote;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rural.pojo.ResponseResult;

import java.util.List;

/**
 *
 */
public interface BooksNoteService extends IService<BooksNote> {

    ResponseResult insertBooksNote(BooksNote booksNote);

    ResponseResult deleteBooksNote(Integer id);

    ResponseResult updateBooksNote(BooksNote booksNote);

    ResponseResult selectBooksNote(Integer id);

    int deleteBatchByChapterId(List<Integer> ids);
}
