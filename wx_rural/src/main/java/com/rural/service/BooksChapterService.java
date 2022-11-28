package com.rural.service;

import com.rural.pojo.BooksChapter;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rural.pojo.ResponseResult;

/**
 *
 */
public interface BooksChapterService extends IService<BooksChapter> {

    ResponseResult insertBooksChapter(BooksChapter booksChapter);

    ResponseResult deleteBooksChapter(Integer id);

    ResponseResult updateBooksChapter(BooksChapter booksChapter);

    ResponseResult selectBooksChapterWx(Integer size, Integer current, Integer id);

    ResponseResult selectBooksChapter(Integer size, Integer current, Integer id, String msg, String type);

}
