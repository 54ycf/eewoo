package com.eewoo.platform.service.impl;

import com.eewoo.common.pojo.User;
import com.eewoo.common.pojo.Visitor;
import com.eewoo.common.security.LoginUser;
import com.eewoo.platform.mapper.VisitorMapper;
import com.eewoo.platform.pojo.vo.response.VisitorResponse;
import com.eewoo.platform.service.VisitorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class VisitorServiceImpl implements VisitorService {
    @Autowired
    VisitorMapper visitorMapper;

    @Override
    public VisitorResponse getInfo() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        Visitor info = visitorMapper.getInfo(id);
        VisitorResponse visitorResponse = new VisitorResponse();
        BeanUtils.copyProperties(info, visitorResponse);
        return visitorResponse;
    }
}
