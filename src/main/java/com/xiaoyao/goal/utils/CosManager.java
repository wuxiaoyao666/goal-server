package com.xiaoyao.goal.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.xiaoyao.goal.config.CosConfig;
import com.xiaoyao.goal.exception.GoalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author 逍遥
 */
@Component
public class CosManager {

    @Autowired
    private CosConfig qiNiuCosConfig;

    public void upload(String key, File file) {
        // 构造一个带指定 Region 对象的配置类
        Configuration cfg = Configuration.create(Region.qvmRegion1());
        // 指定分片上传版本
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(qiNiuCosConfig.getAccessKey(), qiNiuCosConfig.getSecretKey());
        String upToken = auth.uploadToken(qiNiuCosConfig.getBucket());
        try {
            uploadManager.put(file, key, upToken);
        } catch (QiniuException ex) {
            throw new GoalException(ex.getMessage());
        }
    }
}
