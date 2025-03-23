package com.xiaoyao.flow.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @author 逍遥
 */
@Data
public class TagVO {
    private Long id;
    private String name;
    private List<TagVO> children;
}
