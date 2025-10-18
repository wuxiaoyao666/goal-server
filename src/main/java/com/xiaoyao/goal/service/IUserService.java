package com.xiaoyao.goal.service;

import com.xiaoyao.goal.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoyao.goal.entity.dto.RegisterDTO;
import com.xiaoyao.goal.entity.dto.UpdateUserDTO;
import com.xiaoyao.goal.entity.vo.UserInfoVO;
import com.xiaoyao.goal.entity.dto.LoginDTO;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author 逍遥
 * @since 2025-03-18
 */
public interface IUserService extends IService<User> {
    /**
     * 用户登陆
     *
     * @param param 登陆参数
     */
    UserInfoVO login(LoginDTO param);

    /**
     * 用户注册
     *
     * @param body 注册参数
     */
    void register(RegisterDTO body);

    /**
     * 修改用户信息
     *
     * @param body 修改参数
     */
    void update(UpdateUserDTO body);
}
