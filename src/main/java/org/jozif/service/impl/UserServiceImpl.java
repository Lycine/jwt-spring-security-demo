package org.jozif.service.impl;

import lombok.extern.apachecommons.CommonsLog;
import org.jozif.mapper.UserMapper;
import org.jozif.po.User;
import org.jozif.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@CommonsLog
@Service("userService")
public class UserServiceImpl implements UserService {
    private UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public User userFindAll() {
        return userMapper.selectByPrimaryKey(Long.valueOf(1));
    }
}
