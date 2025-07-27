package com.xiaoyao.goal;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.nio.file.Paths;

/**
 * @author 逍遥
 */
public class GeneratorCodeTest {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://124.221.136.224:3306/goal?useUnicode=true&characterEncoding=utf-8", "root", "Senge520...")
                .globalConfig(builder -> builder
                        .author("逍遥")
                        .outputDir(Paths.get(System.getProperty("user.dir")) + "/src/main/java")
                        .commentDate("yyyy-MM-dd")
                )
                .packageConfig(builder -> builder
                        .parent("com.xiaoyao.goal")
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
