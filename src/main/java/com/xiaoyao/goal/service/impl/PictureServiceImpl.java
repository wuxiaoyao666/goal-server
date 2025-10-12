package com.xiaoyao.goal.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import com.xiaoyao.goal.config.CosConfig;
import com.xiaoyao.goal.constant.GoalConstant;
import com.xiaoyao.goal.entity.vo.PictureVO;
import com.xiaoyao.goal.exception.GoalException;
import com.xiaoyao.goal.service.PictureService;
import com.xiaoyao.goal.utils.CosManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;

/**
 * @author 逍遥
 */
@Slf4j
@Service
public class PictureServiceImpl implements PictureService {

    @Autowired
    private CosConfig cosConfig;

    @Autowired
    private CosManager cosManager;

    @Override
    public PictureVO upload(MultipartFile file, Long id) {
        if (file == null || file.isEmpty())
            throw new GoalException("文件不能为空");
        // 1. 校验文件后缀
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        if (!GoalConstant.AllowUploadImageList.contains(suffix))
            throw new GoalException("不支持的文件类型");
        // 2. 生成新的图片文件名和目录
        String uuid = RandomUtil.randomString(16);
        String uploadFileName = String.format("%s_%s.%s", DateUtil.formatDate(new Date()), uuid, suffix);
        String uploadPath = String.format("%s/%s", id, uploadFileName);
        File tempFile = null;
        try {
            tempFile = File.createTempFile(uploadPath, null);
            file.transferTo(tempFile);
            // 3. 上传图片
            cosManager.upload(uploadPath, tempFile);
            return new PictureVO(cosConfig.getHost() + uploadPath,file.getSize(),uploadFileName);
        } catch (Exception e) {
            throw new GoalException(e.getMessage());
        } finally {
            if (tempFile != null) {
                boolean deleteResult = tempFile.delete();
                if (!deleteResult) {
                    log.error("file delete error,file:{}", tempFile.getAbsolutePath());
                }
            }
        }
    }
}
