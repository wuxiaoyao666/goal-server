package com.xiaoyao.flow.entity.dto;

import com.xiaoyao.flow.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author 逍遥
 */
@Data
public class QueryTaskDTO {
    /**
     * 主标签
     */
    @NotBlank(message = "主标签不能为空")
    private String firstTag;
    /**
     * 子标签
     */
    @NotBlank(message = "子标签不能为空")
    private String secondTag;

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private LocalDate startDate;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private LocalDate finishDate;

    /**
     * 状态：默认已完成
     */
    private Byte status = TaskStatus.COMPLETED.getValue();

    /**
     * 平台
     */
    private Byte platform;

    /**
     * 当前页
     */
    private int current = 1;

    /**
     * 每页展示条数
     */
    private int limit = 5;
}
