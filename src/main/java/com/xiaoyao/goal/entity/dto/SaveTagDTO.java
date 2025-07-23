package com.xiaoyao.goal.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SaveTagDTO {
    private Long id;
    @NotBlank(message = "标签名不能为空")
    private String name;
    private Long parent = 0L;
}
