package com.xiaoyao.goal.entity.vo;

import java.time.LocalDate;

/**
 * @author 逍遥
 */
public record SearchDiaryVO(String title, String content, LocalDate createDate) {
}
