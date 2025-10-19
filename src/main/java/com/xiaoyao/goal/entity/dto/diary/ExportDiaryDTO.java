package com.xiaoyao.goal.entity.dto.diary;

import com.xiaoyao.goal.enums.DiaryFileType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author 逍遥
 */
@Data
public class ExportDiaryDTO {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private DiaryFileType type = DiaryFileType.SG;
}
