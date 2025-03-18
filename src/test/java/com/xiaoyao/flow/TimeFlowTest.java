package com.xiaoyao.flow;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Paths;

/**
 * @author 逍遥
 */
public class TimeFlowTest {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://117.72.82.38:3306/time_flow?useUnicode=true&characterEncoding=utf-8", "root", "root")
                .globalConfig(builder -> builder
                        .author("逍遥")
                        .outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java")
                        .commentDate("yyyy-MM-dd")
                )
                .packageConfig(builder -> builder
                        .parent("com.xiaoyao.flow")
                        .entity("entity")
                        .mapper("mapper")
                        .service("service")
                        .serviceImpl("service.impl")
                        .xml("mapper.xml")
                )
                .strategyConfig(builder -> builder
                        .entityBuilder()
                        .enableLombok()
                )
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
