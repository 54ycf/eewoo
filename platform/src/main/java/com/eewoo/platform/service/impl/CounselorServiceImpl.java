package com.eewoo.platform.service.impl;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.common.pojo.User;
import com.eewoo.common.security.LoginUser;
import com.eewoo.platform.mapper.CounselorMapper;
import com.eewoo.platform.pojo.vo.response.Consult;
import com.eewoo.platform.pojo.vo.response.SupervisorResponse;
import com.eewoo.platform.service.CounselorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;

@Service
public class CounselorServiceImpl implements CounselorService {


    @Autowired
    CounselorMapper mapper;

    @Override
    public List<Consult> getConsult(Integer page, Integer size) {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer c_id = user.getId();

        PageHelper.startPage(page,size);
        List<Consult> users = mapper.getAllConsults(c_id);
        PageInfo<Consult> pageInfo = new PageInfo<>();
        return pageInfo.getList();
        /*
         * 通过token获取counselor用户的id，交给mapper层返回符合资格的访客列表
         */
        // return mapper.getAllConsults(c_id);
    }

    @Override
    public int createEvaluation(Integer sessionID, String feedback, String type) {
          mapper.createEvaluation(sessionID, feedback, type);
          mapper.endSession(sessionID, new Date());
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

    @Override
    public Integer getTotalSessionTime() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        return mapper.getTotalSessionTime( id );
    }

    @Override
    public Integer getTodaySessionNum() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        Date today =new Date();
        return mapper.getTodaySessionNum(id,today);
    }

    @Override
    public Integer getTodaySessionTime() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        return mapper.getTodaySessionTime(id);

    }

    @Override
    public Counselor getAllInfoExceptPassword() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        return mapper.getmassiveInfo(id);
    }

//    @Override
//    public Session fetchSessionIfAuthenticated(Integer item) {
//        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
//        Integer id = user.getId();
//        return mapper.getSessionIfOk(id,item);
//    }
}
