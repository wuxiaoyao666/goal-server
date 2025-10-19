package com.xiaoyao.goal.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.xiaoyao.goal.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoyao.goal.entity.dto.tag.QueryTagDTO;
import com.xiaoyao.goal.entity.vo.TagVO;

import java.util.List;

/**
 * <p>
 * 标签表 服务类
 * </p>
 *
 * @author 逍遥
 * @since 2025-03-18
 */
public interface ITagService extends IService<Tag> {
    /**
     * 树形菜单返回 Tag
     * @return List<TagVO>
     */
    List<TagVO> tree();

    /**
     * 分页条件查询
     * @param param 参数
     * @return 查询结果
     */
    IPage<Tag> page(QueryTagDTO param);

    /**
     * 删除标题
     * @param id 唯一标识
     */
    void delete(Long id);
}
