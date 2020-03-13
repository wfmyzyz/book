package com.wfmyzyz.book.service;

import com.wfmyzyz.book.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Miss.Mo
 * @since 2020-03-11
 */
public interface IUserService extends IService<User> {

    /**
     * 根据用户名获取用户
     * @param username
     * @return
     */
    User getUserByUsername(String username);
}
