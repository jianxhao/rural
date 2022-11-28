package com.rural.service;

import com.rural.pojo.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xh
 * @create 2022-11-19  16:30
 */
public interface FileUploadService {
    ResponseResult uploadLocal(MultipartFile file);

    ResponseResult uploadsLocal(MultipartFile[] file);
}
