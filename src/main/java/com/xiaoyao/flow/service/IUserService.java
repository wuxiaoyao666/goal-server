package com.xiaoyao.flow.service;

import com.xiaoyao.flow.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoyao.flow.entity.dto.RegisterDTO;
import com.xiaoyao.flow.entity.vo.UserVo;
import com.xiaoyao.flow.entity.dto.LoginDTO;

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
     * @param param 登陆参数
     */
    UserVo login(LoginDTO param);

    /**
     * 用户注册
     * @param body 注册参数
     */
    void register(RegisterDTO body);
}
