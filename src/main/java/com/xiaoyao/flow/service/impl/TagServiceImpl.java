package com.xiaoyao.flow.service.impl;

import com.xiaoyao.flow.entity.Tag;
import com.xiaoyao.flow.mapper.TagMapper;
import com.xiaoyao.flow.service.ITagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
