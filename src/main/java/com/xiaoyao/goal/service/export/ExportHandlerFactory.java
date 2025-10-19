package com.xiaoyao.goal.service.export;

import com.xiaoyao.goal.enums.DiaryFileType;
import com.xiaoyao.goal.service.export.impl.HtmlExportHandler;
import com.xiaoyao.goal.service.export.impl.MdExportHandler;
import com.xiaoyao.goal.service.export.impl.SgExportHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 逍遥
 */
public class ExportHandlerFactory {

    private static final Map<DiaryFileType, ExportHandler> Handlers = new HashMap<>();

    static {
        Handlers.put(DiaryFileType.SG, new SgExportHandler());
        Handlers.put(DiaryFileType.HTML, new HtmlExportHandler());
        Handlers.put(DiaryFileType.MD, new MdExportHandler());
    }

    public static ExportHandler getHandler(DiaryFileType fileType) {
        ExportHandler handler = Handlers.get(fileType);
        if (handler == null) {
            throw new IllegalArgumentException("不支持的导出类型: " + fileType);
        }
        return handler;
    }
}
