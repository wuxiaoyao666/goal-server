package com.xiaoyao.flow.service;

import com.xiaoyao.flow.entity.Tag;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoyao.flow.entity.dto.QueryTagDTO;
import com.xiaoyao.flow.entity.vo.TagVO;

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
     * 获取所有的一级标签
     * @param param 参数
     * @return 查询结果
     */
    List<Tag> find(QueryTagDTO param);

    /**
     * 删除标题
     * @param id
     */
    void delete(Long id);
}
