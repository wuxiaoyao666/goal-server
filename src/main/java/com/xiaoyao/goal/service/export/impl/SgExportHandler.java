package com.xiaoyao.goal.service.export.impl;

import com.xiaoyao.goal.entity.Diary;
import com.xiaoyao.goal.service.export.ExportHandler;
import com.xiaoyao.goal.utils.KryoSerializer;

import java.util.List;

/**
 * @author 逍遥
 */
public class SgExportHandler implements ExportHandler {
    @Override
    public byte[] export(List<Diary> diaries) {
        return KryoSerializer.serialize(diaries);
    }

    @Override
    public String getContentType() {
        return "application/octet-stream";
    }
}
