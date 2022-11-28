package com.rural.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rural.pojo.Logger;
import com.rural.pojo.ResponseResult;
import com.rural.pojo.TelephoneBook;
import com.rural.service.LoggerService;
import com.rural.service.TelephoneBookService;
import com.rural.mapper.TelephoneBookMapper;
import com.rural.utils.PageInfoUtils;
import com.rural.utils.SecurityContextHolderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 */
@Service
public class TelephoneBookServiceImpl extends ServiceImpl<TelephoneBookMapper, TelephoneBook>
    implements TelephoneBookService{
    private final static String TYPE_NAME = "热线通";
    @Autowired
    private TelephoneBookMapper telephoneBookMapper;
    @Autowired
    private LoggerService loggerService;

    /**
     * 查询热线通全部记录
     * @return 返回ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectByList(Integer size, Integer current) {
        Page<TelephoneBook> page = new Page<>(current,size);
        try {
            Page<TelephoneBook> telephoneBooks = telephoneBookMapper.selectAll(page);
            Map<String,Object> map = new HashMap<>();
            map.put("info",telephoneBooks.getRecords());
            Map<String, Object> pageInfo = PageInfoUtils.getPageInfo(telephoneBooks);
            map.put("pageInfo",pageInfo);
            return new ResponseResult(200,"查询成功",map);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }

    }
    /**
     * 添加热线通记录
     * @return 返回ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult insertTelephoneBook(TelephoneBook telephoneBook) {

        // 判断电话号是否存在
        TelephoneBook telephone = this.selectByPhone(telephoneBook.getPhone());
        if(!Objects.isNull(telephone)){
            return new ResponseResult(202,"电话号已存在");
        }

        // 创建时间
        LocalDate localDate = LocalDate.now();
        telephoneBook.setCreateTime(localDate);
        try {
            int result = telephoneBookMapper.insert(telephoneBook);
            if(result > 0){
                // 添加日志信息
                Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
                Logger logger = new Logger();
                logger.setUserId(userId);
                logger.setModule(TYPE_NAME);
                logger.setNode("添加电话: "+ telephoneBook.getPhone());
                loggerService.insertLoggerInfo(logger);
                // 返回
                return new ResponseResult(200,"添加成功");
            }else{
                return new ResponseResult(201,"添加失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"添加失败");
        }
    }

    /**
     * 删除热线通记录
     * @return 返回ResponseResult(code,msg)
     */
    @Override
    public ResponseResult deleteTelephoneBook(Integer id) {
        try {
            int result = telephoneBookMapper.deleteById(id);
            if(result > 0){
                // 添加日志信息
                Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
                Logger logger = new Logger();
                logger.setUserId(userId);
                logger.setModule(TYPE_NAME);
                logger.setNode("删除电话id: "+ id);
                loggerService.insertLoggerInfo(logger);
                // 返回
                return new ResponseResult(200,"删除成功");
            }else{
                return new ResponseResult(201,"删除失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"删除失败");
        }
    }

    /**
     * 修改热线通记录
     * @return 返回ResponseResult(code,msg)
     */
    @Override
    public ResponseResult updateTelephoneBook(TelephoneBook telephoneBook) {
        // 查找修改前的电话号
        try {
            String telephone = telephoneBookMapper.selectById(telephoneBook.getId()).getPhone();
            // 判断修改后电话号是否相等
            if(!telephone.equals(telephoneBook.getPhone())){
                // 查找新的电话号是否存在
                TelephoneBook telephone_ = this.selectByPhone(telephoneBook.getPhone());
                if(!Objects.isNull(telephone_)){
                    return new ResponseResult(202,"电话号已存在");
                }
            }
            // 添加日志信息
            Integer userId = SecurityContextHolderUtil.getUserIdInAdminUserDetails();
            Logger logger = new Logger();
            logger.setUserId(userId);
            logger.setModule(TYPE_NAME);
            logger.setNode("修改电话号\""+ telephone + "\"为\""+telephoneBook.getPhone()+"\"");
            loggerService.insertLoggerInfo(logger);
            // 执行修改操作
            int result = telephoneBookMapper.updateById(telephoneBook);
            if(result > 0){
                return new ResponseResult(200,"修改成功");
            }else{
                return new ResponseResult(201,"修改失败");
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"修改失败");
        }

    }

    /**
     * 通过 电话号 查找
     * @param phone
     * @return
     */
    @Override
    public TelephoneBook selectByPhone(String phone) {
        QueryWrapper<TelephoneBook> wrapper = new QueryWrapper<>();
        wrapper.eq("phone",phone);
        return telephoneBookMapper.selectOne(wrapper);
    }

    /**
     * 根据typeId查询热线通全部记录
     * @return 返回ResponseResult(code,msg,data)
     */
    @Override
    public ResponseResult selectByListById(Integer id) {
        // 根据typeId查询
        try{
            List<TelephoneBook> telephoneBooks = telephoneBookMapper.selectAllById(id);
            return new ResponseResult(200,"查询成功",telephoneBooks);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseResult(201,"查询失败");
        }
    }

}




