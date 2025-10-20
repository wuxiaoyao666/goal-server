package com.xiaoyao.goal.service.export.impl;

import com.xiaoyao.goal.constant.GoalConstant;
import com.xiaoyao.goal.entity.Diary;
import com.xiaoyao.goal.service.export.ExportHandler;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author 逍遥
 */
public class HtmlExportHandler implements ExportHandler {
    private static final String HTML_TEMPLATE = """
            <!DOCTYPE html>
            <html lang="zh-CN">
            <head>
                <meta charset="UTF-8">
                <title>日记导出</title>
                <style>
                    body { font-family: 'Segoe UI', 'Microsoft YaHei', sans-serif; line-height: 1.6; }
                    .diary-container { max-width: 800px; margin: 0 auto; padding: 20px; }
                    .diary-entry { margin-bottom: 40px; border-bottom: 1px solid #eee; padding-bottom: 20px; }
                    .diary-header { margin-bottom: 15px; }
                    .diary-title { font-size: 1.5rem; margin: 0 0 5px; color: #333; }
                    .diary-meta { color: #666; font-size: 0.9rem; }
                    .diary-tags { margin-top: 5px; }
                    .tag { display: inline-block; background: #f0f0f0; padding: 2px 8px; border-radius: 3px; margin-right: 5px; }
                    .diary-content { margin-top: 15px; }
                </style>
            </head>
            <body>
                <div class="diary-container">
                    <h1 style="text-align: center; margin-bottom: 30px;">我的日记</h1>
                    %s
                </div>
            </body>
            </html>
            """;

    @Override
    public byte[] export(List<Diary> diaries) {
        StringBuilder contentBuilder = new StringBuilder();
        for (Diary diary : diaries) {
            contentBuilder.append(buildDiaryEntry(diary));
        }
        String fullHtml = String.format(HTML_TEMPLATE, contentBuilder);
        return fullHtml.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getContentType() {
        return "text/html;charset=UTF-8";
    }

    private String buildDiaryEntry(Diary diary) {
        return String.format("""
            <div class="diary-entry">
                <div class="diary-header">
                    <h2 class="diary-title">%s</h2>
                    <div class="diary-meta">
                        <span>创建时间：%s</span>
                        <span style="margin-left: 15px;">更新时间：%s</span>
                    </div>
                    %s
                </div>
                <div class="diary-content">%s</div>
            </div>
            """,
                escapeHtml(diary.getTitle()),
                formatDateTime(diary.getCreateTime()),
                formatDateTime(diary.getUpdateTime()),
                buildTagsHtml(diary.getTags()),
                diary.getContent()  // 直接使用原始HTML内容
        );
    }

    private String buildTagsHtml(List<String> tags) {
        if (tags == null || tags.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder("<div class=\"diary-tags\">");
        for (String tag : tags) {
            sb.append(String.format("<span class=\"tag\">%s</span>", escapeHtml(tag)));
        }
        sb.append("</div>");
        return sb.toString();
    }

    private String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        return dateTime.format(DateTimeFormatter.ofPattern(GoalConstant.DefaultDateTimeFormat));
    }

    private String escapeHtml(String input) {
        return StringUtils.hasText(input) ?
                input.replace("&", "&amp;")
                        .replace("<", "&lt;")
                        .replace(">", "&gt;")
                        .replace("\"", "&quot;")
                        .replace("'", "&#39;")
                : "";
    }
}
