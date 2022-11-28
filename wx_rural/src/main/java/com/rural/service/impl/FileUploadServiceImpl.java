package com.rural.service.impl;

import com.rural.pojo.ResponseResult;
import com.rural.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

/**
 * @author xh
 * @create 2022-11-19  16:30
 */
@Service
@Slf4j
public class FileUploadServiceImpl implements FileUploadService {
    // 本地上传文件
    @Override
    public ResponseResult uploadLocal(MultipartFile file) {
        log.info("file={}",file.getSize());
        String localDate = LocalDate.now().toString();
        String path = "/home/images/"+localDate+"/"; // 本地路径，绝对路径。
//        log.info(path);
        File f = new File(path);
        // 判断文件是否存在，不存在先创建
        if(!f.exists()){
            f.mkdirs();
        }
        // 获取文件名
        String originalFilename = file.getOriginalFilename();
        // 取出文件后缀
        String sufName = originalFilename.substring(originalFilename.lastIndexOf('.'));
        // 使用UUID防止重名，组合后缀获取新的完整文件名
        String fileName = UUID.randomUUID().toString()+sufName;
//        log.info(fileName); // Slj4 打印文件名
        try {
            // 上传
            file.transferTo(new File(path,fileName));
            System.out.println();
            System.out.println("上传成功");
            System.out.println("图片路径==》"+"http://43.139.181.224:8080"+path+fileName);
            // 返回给前端，自己定义
            Map<String,Object> map = new HashMap<>();
            map.put("imgUrl","http://43.139.181.224:8080"+path+fileName);
            return new ResponseResult(200,"上传成功",map);
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("上传失败");
            return new ResponseResult(201,"上传失败");
        }
    }
    // 本地上传文件(多文件)
    @Override
    public ResponseResult uploadsLocal(MultipartFile[] files) {
        Map<String,List<String>> map = new HashMap<>();
        List<String> urls = new ArrayList<>();
        String localDate = LocalDate.now().toString();
        String path = "/home/images/"+localDate+"/"; // 本地路径，绝对路径。
        File f = new File(path);
        // 判断文件是否存在，不存在先创建
        if(!f.exists()){
            f.mkdirs();
        }
        try {
            for (MultipartFile file : files) {
                // 获取文件名
                String originalFilename = file.getOriginalFilename();
                // 取出文件后缀
                String sufName = originalFilename.substring(originalFilename.lastIndexOf('.'));
                // 使用UUID防止重名，组合后缀获取新的完整文件名
                String fileName = UUID.randomUUID().toString() + sufName;
                // 上传
                file.transferTo(new File(path, fileName));
                System.out.println();
                System.out.println("上传成功");
                System.out.println("图片路径==》" + "http://43.139.181.224:8080" + path + fileName);
                // 返回给前端，自己定义
                System.out.println("上传成功");
                urls.add("http://43.139.181.224:8080" + path + fileName);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("上传失败");
            return new ResponseResult(201, "上传失败");
        }
        map.put("imgUrls",urls);
        return new ResponseResult(200, "上传成功",map);
    }
}
