package com.xiaoyao.goal.entity.dto.tag;

import com.xiaoyao.goal.constant.GoalConstant;
import lombok.Data;

@Data
public class QueryTagDTO {
    private Long parent;
    private int current = 1;
    private int limit = GoalConstant.DefaultPageLimit;
}
