package com.xiaoyao.flow.utils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtils {

    /**
     * 计算耗时
     * @param startDateTime 开始时间
     * @param endDateTime 结束时间
     * @return 耗时
     */
    public static String calculateDuration(LocalDateTime startDateTime, LocalDateTime endDateTime) {

        // 计算天数差（跨天则补 24 小时）
        long daysDiff = ChronoUnit.DAYS.between(startDateTime, endDateTime);
        if (daysDiff < 0) {
            throw new IllegalArgumentException("结束日期不能早于开始日期！");
        }

        // 计算时间差（转换为秒）
        long startSeconds = startDateTime.toLocalTime().toSecondOfDay();
        long endSeconds = endDateTime.toLocalTime().toSecondOfDay();
        long totalSeconds = endSeconds - startSeconds + daysDiff * 24 * 3600;

        // 处理负数（例如：跨天后 endTime < startTime）
        if (totalSeconds < 0) {
            totalSeconds += 24 * 3600;
        }

        // 转换为时、分、秒
        long hours = totalSeconds / 3600;
        long minutes = (totalSeconds % 3600) / 60;
        long seconds = totalSeconds % 60;

        return String.format("%d 时 %d 分 %d 秒", hours, minutes, seconds);
    }
}
