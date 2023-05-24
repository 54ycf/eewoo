package com.eewoo.platform.service.impl;

import com.eewoo.common.util.Constant;
import com.eewoo.platform.mapper.UserMapper;
import com.eewoo.platform.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    UserMapper userMapper;

    @Override
    public int disableUser(Integer id, String role) {
        return userMapper.disableUser(id, Constant.roleTableMap.get(role));
    }
}
