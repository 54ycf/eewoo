package com.eewoo.platform.service.impl;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.common.pojo.User;
import com.eewoo.common.pojo.vo.request.SessionSCRequest;
import com.eewoo.common.security.LoginUser;
import com.eewoo.platform.mapper.CounselorMapper;
import com.eewoo.platform.pojo.model.SessionSC;
import com.eewoo.platform.pojo.vo.response.Consult;
import com.eewoo.platform.pojo.vo.response.DayScheduleCounselorResponse;
import com.eewoo.platform.pojo.vo.response.ScheduleCounselorResponse;
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
    CounselorMapper counselorMapper;

    @Override
    public PageInfo<Consult> getConsult(Integer page, Integer size) {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer c_id = user.getId();//得到本咨询师聊天的访客的简略会话信息
        PageHelper.startPage(page,size);
        List<Consult> users = counselorMapper.getAllConsults(c_id);
        PageInfo<Consult> pageInfo = new PageInfo<>(users);
        return pageInfo;
        /*
         * 通过token获取counselor用户的id，交给mapper层返回符合资格的访客列表
         */
        // return mapper.getAllConsults(c_id);
    }

    @Override
    public int createEvaluation(Integer sessionID, String feedback, String type) {
          counselorMapper.createEvaluation(sessionID, feedback, type);
          counselorMapper.endSession(sessionID, new Date());
          return 1;
    }

    @Override
    public SupervisorResponse findSupervisor(Integer counselorId) {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        Supervisor info = counselorMapper.findSupervisor(id);
        SupervisorResponse supervisorResponse = new SupervisorResponse();
        BeanUtils.copyProperties(info, supervisorResponse);
        return supervisorResponse;
    }

    @Override
    public Integer getTotalSessionTime() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        return counselorMapper.getTotalSessionTime( id );
    }

    @Override
    public Integer getTodaySessionNum() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        Date today =new Date();
        return counselorMapper.getTodaySessionNum(id,today);
    }

    @Override
    public Integer getTodaySessionTime() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        return counselorMapper.getTodaySessionTime(id);

    }

    @Override
    public Counselor getAllInfoExceptPassword() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        return counselorMapper.getmassiveInfo(id);
    }

    @Override
    public List<ScheduleCounselorResponse> getCounselorSchedules() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        System.out.println(id);
        List<ScheduleCounselorResponse> scheduleCounselor = counselorMapper.getPersonalScheduleByWeek(id);
        return scheduleCounselor;
    }

    @Override
    public List<DayScheduleCounselorResponse> getSuperVisorSchedulesByDay() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        return counselorMapper.getPersonalScheduleByDay(id);
    }

    @Override
    public Supervisor getBindSupervisor() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        return counselorMapper.getSupervisor(id);
    }

    @Override
    public Integer createSCSessionAndFetchID(SessionSCRequest sessionSCRequest) {
//        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
//        Integer counselor_id = user.getId();// get counselor's id here
        // reference to visitor's service layer to learn something
        SessionSC session = new SessionSC();
        BeanUtils.copyProperties(sessionSCRequest,session);
        if(counselorMapper.insertSCSession(session) == 1)
            return session.getId();
        else
            return -1;
    }
}
