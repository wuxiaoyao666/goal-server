package com.xiaoyao.flow.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaoyao.flow.entity.Tag;
import com.xiaoyao.flow.entity.dto.QueryTagDTO;
import com.xiaoyao.flow.mapper.TagMapper;
import com.xiaoyao.flow.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyao.flow.entity.vo.TagVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 标签表 服务实现类
 * </p>
 *
 * @author 逍遥
 * @since 2025-03-18
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements ITagService {

    @Override
    public List<TagVO> tree() {
        // 查询所有标签
        List<Tag> tags = list(Wrappers.lambdaQuery(Tag.class).eq(Tag::getUserId, StpUtil.getLoginIdAsInt()));
        // 存储 Vo 对象
        Map<Long, TagVO> voMap = new HashMap<>();
        List<TagVO> roots = new ArrayList<>();
        for (Tag tag : tags) {
            TagVO vo = new TagVO();
            vo.setId(tag.getId());
            vo.setName(tag.getName());
            vo.setChildren(new ArrayList<>());
            voMap.put(tag.getId(), vo);
        }
        // 建立父子关系
        for (Tag tag : tags) {
            TagVO current = voMap.get(tag.getId());
            Long parentId = tag.getParent();
            if (parentId == 0) roots.add(current);
            else {
                TagVO parent = voMap.get(parentId);
                parent.getChildren().add(current);
            }
        }
        return roots;
    }

    @Override
    public List<Tag> find(QueryTagDTO param) {
        LambdaQueryWrapper<Tag> wrapper = Wrappers.lambdaQuery(Tag.class).eq(Tag::getUserId, StpUtil.getLoginIdAsInt());
        if (param.getParent() != null) {
            wrapper.eq(Tag::getParent, param.getParent());
        }
        return list(wrapper);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        removeById(id);
        if (id > 0) {
            // 删除所有二级子标题
            List<Tag> tags = list(Wrappers.lambdaQuery(Tag.class).select(Tag::getId).eq(Tag::getParent, id));
            if (!tags.isEmpty()) {
                // 级联删除
                List<Long> tagIds = tags.stream().map(Tag::getId).toList();
                remove(Wrappers.lambdaQuery(Tag.class).in(Tag::getId, tagIds));
            }
        }
    }
}
