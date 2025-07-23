package com.xiaoyao.goal.entity.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 逍遥
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeBO {
    private long hours;
    private long minutes;
    private long seconds;
}
