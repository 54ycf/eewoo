package com.eewoo.platform.service.impl;

import com.eewoo.common.pojo.Session;
import com.eewoo.common.util.Constant;
import com.eewoo.platform.mapper.AdminMapper;
import com.eewoo.platform.mapper.UserMapper;
import com.eewoo.platform.pojo.vo.response.CounselorSupervisorResponse;
import com.eewoo.platform.pojo.vo.response.SessionResponse;
import com.eewoo.platform.service.AdminService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    AdminMapper adminMapper;

    @Override
    public int disableUser(Integer id, String role) {
        return userMapper.disableUser(id, Constant.roleTableMap.get(role));
    }

    @Override
    public List<SessionResponse> getSessions() {
        List<Session> sessions= adminMapper.getSessions();
        List<SessionResponse> sessionResponses=new ArrayList<>();
        for (int i=0;i<sessions.size();i++){
            SessionResponse sessionResponse=new SessionResponse();
            BeanUtils.copyProperties(sessions.get(i),sessionResponse);
            sessionResponses.add(sessionResponse);
        }
        return sessionResponses;
    }

    @Override
    public List<CounselorSupervisorResponse> getCounselors() {
        return adminMapper.getCounselorsAndSupervisors();
    }

    @Override
    public List<CounselorSupervisorResponse> getSupervisors() {
        return adminMapper.getCounselorsAndSupervisors();
    }

}
