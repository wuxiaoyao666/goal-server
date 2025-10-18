package com.xiaoyao.goal.utils;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.xiaoyao.goal.entity.Diary;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Kryo 序列化工具类
 *
 * @author 逍遥
 */
public class KryoSerializer {

    private KryoSerializer() {
    }

    private static final ThreadLocal<Kryo> Kryo = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setReferences(true);
        // 非注册模式，会导致序列化后字节较大
        //kryo.setRegistrationRequired(false);
        // 注册常用类
        kryo.register(Diary.class);
        kryo.register(ArrayList.class);
        kryo.register(LocalDateTime.class);
        return kryo;
    });


    /**
     * 获取当前线程的 Kryo 实例
     *
     * @return Kryo 实例
     */
    private static Kryo getKryo() {
        return Kryo.get();
    }

    /**
     * 序列化对象为字节数组
     *
     * @param <T> 对象的泛型类型
     * @param obj 待序列化的对象
     * @return 序列化后的字节数组
     */
    public static <T> byte[] serialize(T obj) {
        if (obj == null) {
            return null;
        }

        Kryo kryo = getKryo();

        // 建议初始容量设大一些，减少内部数组扩容
        // 实际应用中可以根据平均对象大小调整
        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);

        try (Output output = new Output(baos)) {
            // writeObject(output, obj) 适用于知道对象类型的情况
            // writeClassAndObject(output, obj) 适用于不知道对象确切类型（需要泛型支持）的情况
            kryo.writeClassAndObject(output, obj);
            output.flush();
            return baos.toByteArray();
        }
    }

    /**
     * 从字节数组反序列化为对象
     *
     * @param <T>  对象的泛型类型
     * @param data 序列化后的字节数组
     * @return 反序列化后的对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T deserialize(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }

        Kryo kryo = getKryo();
        ByteArrayInputStream bais = new ByteArrayInputStream(data);

        try (Input input = new Input(bais)) {
            // readClassAndObject() 对应 writeClassAndObject()
            return (T) kryo.readClassAndObject(input);
        }
    }
}
