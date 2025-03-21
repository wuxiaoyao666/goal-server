package com.xiaoyao.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.xiaoyao.flow.entity.User;
import com.xiaoyao.flow.exception.BusinessException;
import com.xiaoyao.flow.mapper.UserMapper;
import com.xiaoyao.flow.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaoyao.flow.utils.BcryptUtils;
import com.xiaoyao.flow.utils.ResultCode;
import com.xiaoyao.flow.vo.UserVo;
import com.xiaoyao.flow.dto.LoginDTO;
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
    public UserVo login(LoginDTO param) {
        // 1. 查询用户
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class)
                .select(User::getId, User::getUsername,User::getPassword)
                .eq(User::getUsername, param.getUsername());
        User user = getOne(wrapper);
        // 2. 用户密码校验
        if(user == null || !BcryptUtils.verify(param.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.USERNAME_PASSWORD_INVALID_EXCEPTION);
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }
}
