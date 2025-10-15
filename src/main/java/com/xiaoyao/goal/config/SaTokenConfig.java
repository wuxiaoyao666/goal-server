package com.xiaoyao.goal.config;

import cn.dev33.satoken.jwt.StpLogicJwtForStateless;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 逍遥
 */
@Configuration
public class SaTokenConfig {
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForStateless();
    }
}
