package com.xiaoyao.goal.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 逍遥
 */
public interface PictureService {

    /**
     * 文件上传
     *
     * @param file 文件
     * @param id   用户 ID
     * @return 文件路径
     */
    String upload(MultipartFile file, Long id);
}
