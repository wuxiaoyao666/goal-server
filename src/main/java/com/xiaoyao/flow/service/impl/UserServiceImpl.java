package com.xiaoyao.flow.service.impl;

import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaoyao.flow.entity.Tag;
import com.xiaoyao.flow.entity.User;
import com.xiaoyao.flow.entity.dto.RegisterDTO;
import com.xiaoyao.flow.exception.BusinessException;
import com.xiaoyao.flow.mapper.UserMapper;
import com.xiaoyao.flow.service.ITagService;
import com.xiaoyao.flow.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyao.flow.utils.BcryptUtils;
import com.xiaoyao.flow.utils.ResultCode;
import com.xiaoyao.flow.entity.vo.UserVo;
import com.xiaoyao.flow.entity.dto.LoginDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author 逍遥
 * @since 2025-03-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private ITagService tagService;

    @Override
    public UserVo login(LoginDTO param) {
        // 1. 查询用户
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class)
                .select(User::getId, User::getUsername, User::getPassword, User::getNickname, User::getAvatar)
                .eq(User::getUsername, param.getUsername());
        User user = getOne(wrapper);
        // 2. 用户密码校验
        if (user == null || !BcryptUtils.verify(param.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USERNAME_PASSWORD_INVALID_EXCEPTION);
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    @Override
    @Transactional
    public void register(RegisterDTO body) {
        User user = new User();
        BeanUtils.copyProperties(body, user);
        user.setPassword(BcryptUtils.hash(user.getPassword()));
        save(user);
        // 初始化 Tag
        String initializeTags = FileUtil.readUtf8String("initialize/initialize_tag.json");
        List<Tag> tags = JSON.parseArray(initializeTags, Tag.class);
        tags.forEach(tag -> {
            tag.setUserId(user.getId());
        });
        tagService.saveBatch(tags);
    }
}
