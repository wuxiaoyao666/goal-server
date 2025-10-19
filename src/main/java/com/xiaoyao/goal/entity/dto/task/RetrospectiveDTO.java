package com.xiaoyao.goal.entity.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * @author 逍遥
 */
@Data
public class RetrospectiveDTO {
    @NotNull(message = "开始时间不能为空")
    private LocalDate startDate;
    @NotNull(message = "结束时间不能为空")
    private LocalDate finishDate;
    private Byte status;
    private Byte platform;
}
