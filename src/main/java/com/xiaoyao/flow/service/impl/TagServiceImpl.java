package com.xiaoyao.flow.service.impl;

import com.xiaoyao.flow.entity.Tag;
import com.xiaoyao.flow.mapper.TagMapper;
import com.xiaoyao.flow.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyao.flow.vo.TagVO;
import org.springframework.stereotype.Service;

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
        List<Tag> tags = list();
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
}
