package com.rural.service;

import com.rural.pojo.ResponseResult;
import com.rural.pojo.TelephoneBook;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface TelephoneBookService extends IService<TelephoneBook> {

    ResponseResult selectByList(Integer size,Integer current);

    ResponseResult insertTelephoneBook(TelephoneBook telephoneBook);

    ResponseResult deleteTelephoneBook(Integer id);

    ResponseResult updateTelephoneBook(TelephoneBook telephoneBook);

    TelephoneBook selectByPhone(String phone);

    ResponseResult selectByListById(Integer id);
}
