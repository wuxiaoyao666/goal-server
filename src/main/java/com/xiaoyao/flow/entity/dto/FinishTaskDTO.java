package com.xiaoyao.flow.entity.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FinishTaskDTO {
    @NotNull(message = "taskId 不能为空")
    private Long taskId;
    private boolean showTask = true;
}
