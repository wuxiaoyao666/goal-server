package com.xiaoyao.goal.cos;

import com.qiniu.common.QiniuException;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.xiaoyao.goal.config.CosConfig;
import com.xiaoyao.goal.exception.GoalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author 逍遥
 */
@Component
@ConditionalOnProperty(prefix = "cos", name = "type", havingValue = "qiniu")
public class QiNiuCosManager implements CosManager{

    @Autowired
    private CosConfig cosConfig;

    @Override
    public void upload(String key, File file) {
        // 构造一个带指定 Region 对象的配置类
        Configuration cfg = Configuration.create(Region.qvmRegion1());
        // 指定分片上传版本
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        Auth auth = Auth.create(cosConfig.getAccessKey(), cosConfig.getSecretKey());
        String upToken = auth.uploadToken(cosConfig.getBucket());
        try {
            uploadManager.put(file, key, upToken);
        } catch (QiniuException ex) {
            throw new GoalException(ex.getMessage());
        }
    }
}
