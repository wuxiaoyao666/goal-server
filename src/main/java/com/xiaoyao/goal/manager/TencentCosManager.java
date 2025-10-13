package com.xiaoyao.goal.manager;

import com.qcloud.cos.COSClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @author 逍遥
 */
@Component
@ConditionalOnProperty(prefix = "cos", name = "type", havingValue = "tencent")
public class TencentCosManager implements CosManager{

    @Autowired
    private COSClient cosClient;

    @Override
    public void upload(String key, File file) {

    }
}
