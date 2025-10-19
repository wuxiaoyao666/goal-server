package com.xiaoyao.goal.service.export;

import com.xiaoyao.goal.entity.Diary;

import java.util.List;

/**
 * @author 逍遥
 */
public interface ExportHandler {

    /**
     * 导出日记
     * @param diaries 日记列表
     * @return 日记二进制数据
     */
    byte[] export(List<Diary> diaries);

    String getContentType();
}
