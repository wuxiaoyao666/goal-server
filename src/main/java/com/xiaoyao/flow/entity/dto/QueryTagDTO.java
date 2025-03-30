package com.xiaoyao.flow.entity.dto;

import com.xiaoyao.flow.constant.TimeFlowConstant;
import lombok.Data;

@Data
public class QueryTagDTO {
    private Long parent;
    private int current = 1;
    private int limit = TimeFlowConstant.DefaultPageLimit;
}
