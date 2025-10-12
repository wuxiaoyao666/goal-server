package com.xiaoyao.goal.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.xiaoyao.goal.service.PictureService;
import com.xiaoyao.goal.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author 逍遥
 */
@RestController
@RequestMapping("/picture")
public class PictureController {

    @Autowired
    private PictureService pictureService;

    @PostMapping("/upload")
    public Result<String> upload(@RequestPart("file") MultipartFile file) {
        return Result.success(pictureService.upload(file, StpUtil.getLoginIdAsLong()));
    }
}
