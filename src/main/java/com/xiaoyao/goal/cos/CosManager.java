package com.xiaoyao.goal.cos;

import java.io.File;

/**
 * @author 逍遥
 */
public interface CosManager {

    /**
     * 上传文件
     *
     * @param key  路径
     * @param file 文件名
     */
    void upload(String key, File file);
}
