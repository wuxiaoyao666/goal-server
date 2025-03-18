package com.xiaoyao.flow.service;

import com.xiaoyao.flow.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xiaoyao.flow.vo.UserVo;
import dto.LoginDTO;

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
     * @return 登陆结果
     */
    UserVo login(LoginDTO param);
}
