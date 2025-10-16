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
     * @param file 文件
     * @return 图片链接
     */
    String upload(String key, File file);
}
