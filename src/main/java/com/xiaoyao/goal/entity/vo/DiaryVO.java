package com.xiaoyao.goal.entity.vo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.xiaoyao.goal.constant.GoalConstant;
import com.xiaoyao.goal.entity.Diary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author 逍遥
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiaryVO {
    /**
     * 唯一标识
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 日记标题
     */
    private String title;

    /**
     * 日记正文
     */
    private String content;

    /**
     * 日记标签
     */
    private List<String> tags;

    /**
     * 内容预览
     */
    private String preview;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    public static DiaryVO toDiaryVO(Diary diary){
        DiaryVO diaryVO = new DiaryVO();
        BeanUtil.copyProperties(diary, diaryVO);
        /*String content = diary.getContent();
        if (!StrUtil.isBlank(content)) {
            // JsonP 去除标签
            Document doc = Jsoup.parse(content);
            content = doc.text().trim();
        }
        String preview = StrUtil.isBlank(content) ? StrUtil.EMPTY : content.substring(0, Math.min(content.length(), 50));
        diaryVO.setPreview(preview);*/
        diaryVO.setCreateTime(DateUtil.format(diary.getCreateTime(), GoalConstant.DefaultDateTimeFormat));
        diaryVO.setUpdateTime(DateUtil.format(diary.getUpdateTime(), GoalConstant.DefaultDateTimeFormat));
        return diaryVO;
    }
}
