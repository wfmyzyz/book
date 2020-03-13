package com.wfmyzyz.book.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wfmyzyz.book.domain.User;
import com.wfmyzyz.book.mapper.UserMapper;
import com.wfmyzyz.book.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Miss.Mo
 * @since 2020-03-11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Override
    public User getUserByUsername(String username) {
        return this.lambdaQuery().eq(User::getUsername,username).last("limit 1").one();
    }
}
