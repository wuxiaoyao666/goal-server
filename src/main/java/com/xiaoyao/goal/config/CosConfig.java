package com.xiaoyao.goal.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author 逍遥
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "qiniu")
public class CosConfig {

    private String host;

    private String accessKey;

    private String secretKey;

    private String bucket;
}
