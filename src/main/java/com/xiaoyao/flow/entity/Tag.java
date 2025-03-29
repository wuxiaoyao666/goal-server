package com.xiaoyao.flow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>
 * 标签表
 * </p>
 *
 * @author 逍遥
 * @since 2025-03-18
 */
@Getter
@Setter
@ToString
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 父标签
     */
    private Long parent;

    /**
     * 用户 ID
     */
    private Integer userId;
}
