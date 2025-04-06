package com.xiaoyao.flow.entity.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author 逍遥
 */
@Data
public class CreatePlanDTO {
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotNull(message = "标签不能为空")
    private Long tagId;
    /// 类型
    @NotNull(message = "Plan 类型不能为空")
    private Byte type;
    /// 开始日期
    private LocalDate startDate;
    /// 开始时间
    private LocalTime startTime;
    /// 结束日期
    private LocalDate endDate;
    /// 结束时间
    private LocalTime endTime;
}
