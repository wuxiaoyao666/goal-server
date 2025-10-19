package com.xiaoyao.goal.service.export.impl;

import com.xiaoyao.goal.entity.Diary;
import com.xiaoyao.goal.service.export.ExportHandler;

import java.util.List;

/**
 * @author 逍遥
 */
public class HtmlExportHandler implements ExportHandler {
    @Override
    public byte[] export(List<Diary> diaries) {
        return new byte[0];
    }

    @Override
    public String getContentType() {
        return "text/html;charset=UTF-8";
    }
}
