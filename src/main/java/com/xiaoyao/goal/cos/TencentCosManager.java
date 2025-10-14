package com.xiaoyao.goal.cos;

import cn.hutool.json.JSONUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.PutObjectResult;
import com.xiaoyao.goal.config.CosConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author 逍遥
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = "cos", name = "type", havingValue = "tencent")
public class TencentCosManager implements CosManager{

    @Autowired
    private CosConfig cosConfig;

    @Autowired
    private COSClient cosClient;

    @Override
    public void upload(String key, File file) {
        PutObjectRequest putObjectRequest = new PutObjectRequest(cosConfig.getBucket(), key, file);
        PutObjectResult putObjectResult = cosClient.putObject(putObjectRequest);
        log.debug(JSONUtil.toJsonStr(putObjectResult));
    }
}
