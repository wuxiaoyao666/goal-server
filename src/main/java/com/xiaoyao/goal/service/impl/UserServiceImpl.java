package com.xiaoyao.goal.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaoyao.goal.entity.User;
import com.xiaoyao.goal.entity.dto.user.RegisterDTO;
import com.xiaoyao.goal.entity.dto.user.UpdateUserDTO;
import com.xiaoyao.goal.exception.BusinessException;
import com.xiaoyao.goal.mapper.UserMapper;
import com.xiaoyao.goal.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyao.goal.utils.BcryptUtils;
import com.xiaoyao.goal.utils.ResultCode;
import com.xiaoyao.goal.entity.vo.UserInfoVO;
import com.xiaoyao.goal.entity.dto.user.LoginDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

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

    @Override
    public UserInfoVO login(LoginDTO param) {
        // 1. 查询用户
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class)
                //.select(User::getId, User::getUsername, User::getPassword, User::getNickname, User::getAvatar)
                .eq(User::getUsername, param.getUsername());
        User user = getOne(wrapper);
        // 2. 用户密码校验
        if (user == null || !BcryptUtils.verify(param.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USERNAME_PASSWORD_INVALID_EXCEPTION);
        }
        UserInfoVO userVo = new UserInfoVO();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }

    @Override
    public void register(RegisterDTO body) {
        User user = new User();
        BeanUtils.copyProperties(body, user);
        user.setPassword(BcryptUtils.hash(user.getPassword()));
        save(user);
    }

    @Override
    public void update(UpdateUserDTO body) {
        User user = new User();
        BeanUtils.copyProperties(body, user);
        user.setId(StpUtil.getLoginIdAsLong());
        updateById(user);
    }
}
