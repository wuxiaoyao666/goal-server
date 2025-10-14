package com.xiaoyao.goal;

import com.xiaoyao.goal.cos.CosManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest
class CosTest {

    @Autowired
    private CosManager cosManager;

    @Test
    void testUpload(){
        cosManager.upload("1/demo.jpg",new File("pom.xml"));
    }
}
