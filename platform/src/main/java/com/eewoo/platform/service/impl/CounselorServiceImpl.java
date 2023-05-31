package com.eewoo.platform.service.impl;

import com.eewoo.common.pojo.Supervisor;
import com.eewoo.common.pojo.User;
import com.eewoo.common.security.LoginUser;
import com.eewoo.platform.mapper.CounselorMapper;
import com.eewoo.platform.pojo.vo.response.Consult;
import com.eewoo.platform.pojo.vo.response.SupervisorResponse;
import com.eewoo.platform.service.CounselorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CounselorServiceImpl implements CounselorService {

    @Autowired
    CounselorMapper mapper;

    @Override
    public List<Consult> getConsult() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        /*
         * 通过token获取counselor用户的id，交给mapper层返回符合资格的访客列表
         */
         return mapper.getAllConsults(id);
    }

    @Override
    public int createEvaluation(Integer sessionID, String feedback, String type) {
          mapper.createEvaluation(sessionID, feedback, type);
          return 1;

    }

    @Override
    public SupervisorResponse findSupervisor(Integer counselorId) {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        Supervisor info = mapper.findSupervisor(id);
        SupervisorResponse supervisorResponse = new SupervisorResponse();
        BeanUtils.copyProperties(info, supervisorResponse);
        return supervisorResponse;
    }
}
