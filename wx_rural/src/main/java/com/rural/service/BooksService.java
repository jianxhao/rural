package com.rural.service;

import com.rural.pojo.Books;
import com.baomidou.mybatisplus.extension.service.IService;
import com.rural.pojo.ResponseResult;

/**
 *
 */
public interface BooksService extends IService<Books> {

    ResponseResult insertBooks(Books books);

    ResponseResult deleteBooks(Integer id);

    ResponseResult updateBooks(Books books);

    ResponseResult updateBooksInStatus(Integer id);

    ResponseResult selectBooksWx();

    ResponseResult selectBooks(Integer size, Integer current, String msg, String type);
}
