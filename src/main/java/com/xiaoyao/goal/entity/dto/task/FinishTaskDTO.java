package com.xiaoyao.goal.entity.dto.task;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class FinishTaskDTO {
    @NotNull(message = "taskId 不能为空")
    private Long taskId;
    private boolean showTask = true;
    private String title;
    private String firstTag;
    private String secondTag;
}
