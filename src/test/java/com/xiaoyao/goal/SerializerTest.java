package com.xiaoyao.goal;

import com.xiaoyao.goal.entity.Diary;
import com.xiaoyao.goal.utils.KryoSerializer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 逍遥
 */
public class SerializerTest {

    static void main() {
        List<String> tags = new ArrayList<>();
        tags.add("标签1");
        Diary diary = Diary.builder()
                .id(1L).title("xixi")
                .content("哈哈哈").createTime(LocalDateTime.now()).tags(tags).build();
        List<Diary> diaries = new ArrayList<>();
        diaries.add(diary);
        byte[] serialize = KryoSerializer.serialize(diaries);
        List<Diary> deserialize = KryoSerializer.deserialize(serialize);
        System.out.println(deserialize);
    }
}
