package com.xiaoyao.flow;

import com.xiaoyao.flow.service.ITagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author 逍遥
 */
@SpringBootTest
class TagTest {

    @Autowired
    ITagService tagService;

    @Test
    void loadTag(){
        tagService.list().forEach(System.out::println);
    }
}
