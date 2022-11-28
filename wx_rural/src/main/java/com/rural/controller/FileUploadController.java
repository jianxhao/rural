package com.rural.controller;

import com.rural.pojo.ResponseResult;
import com.rural.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author xh
 * @create 2022-11-19  16:27
 */
@RestController
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;

    // 本地上传文件
    @PostMapping("/upload")
    @PreAuthorize("hasAnyAuthority('client','admin','root')")
    public ResponseResult uploadController(@RequestParam("file") MultipartFile file){
        return fileUploadService.uploadLocal(file);
    }

    // 本地上传文件(多文件)
    @PostMapping("/uploads")
    @PreAuthorize("hasAnyAuthority('client','admin','root')")
    public ResponseResult uploadsController(@RequestParam("file") MultipartFile[] file){
        return fileUploadService.uploadsLocal(file);
    }
}
