package com.eewoo.platform.service.impl;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.ScheduleCounselor;
import com.eewoo.common.pojo.ScheduleSupervisor;
import com.eewoo.common.pojo.Visitor;
import com.eewoo.common.util.Constant;
import com.eewoo.platform.mapper.AdminMapper;
import com.eewoo.platform.mapper.UserMapper;
import com.eewoo.platform.mapper.VisitorMapper;
import com.eewoo.platform.pojo.vo.request.ScheduleCounselorRequest;
import com.eewoo.platform.pojo.vo.request.ScheduleSupervisorRequest;
import com.eewoo.platform.pojo.vo.response.*;
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

    @Autowired
    VisitorMapper visitorMapper;

    @Override
    public int disableUser(Integer id, String role) {
        return userMapper.disableUser(id, Constant.roleTableMap.get(role));
    }

    @Override
    public List<SessionResponse> getSessions() {
        List<SessionResponse> sessionResponses= adminMapper.getSessions();
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

    @Override
    public int removeCounselor(Integer id) {
        int ok=adminMapper.deleteCounselor(id);
        return ok;
    }

    @Override
    public List<VisitorResponse> getVistors() {
        List<Visitor> visitors= adminMapper.getVisitors();
        List<VisitorResponse> visitorResponses=new ArrayList<>();
        for (int i=0;i<visitors.size();i++){
            VisitorResponse visitorResponse=new VisitorResponse();
            BeanUtils.copyProperties(visitors.get(i),visitorResponse);
            visitorResponses.add(visitorResponse);
        }
        return visitorResponses;
    }

    @Override
    public List<CounselorResponse> getTopSessions() {
        List<SessionResponse> sessionResponses=adminMapper.getTopSessions();
        List<CounselorResponse> counselorResponses=new ArrayList<>();
        for (int i=0;i<sessionResponses.size();i++){
            Counselor counselor=visitorMapper.getHistoryCounselor(sessionResponses.get(i).getCounselorId());
            CounselorResponse counselorResponse=new CounselorResponse();
            BeanUtils.copyProperties(counselor,counselorResponse);
            counselorResponses.add(counselorResponse);
        }
        return counselorResponses;
    }

    @Override
    public List<CounselorResponse> getTopScoreCounselors() {
        List<SessionResponse> sessionResponses=adminMapper.getTopScoreCounselors();
        List<CounselorResponse> counselorResponses=new ArrayList<>();
        for (int i=0;i<sessionResponses.size();i++){
            Counselor counselor=visitorMapper.getHistoryCounselor(sessionResponses.get(i).getCounselorId());
            CounselorResponse counselorResponse=new CounselorResponse();
            BeanUtils.copyProperties(counselor,counselorResponse);
            counselorResponses.add(counselorResponse);
        }
        return counselorResponses;

    }

    @Override
    public List<ScheduleCounselorResponse> getCounselorSchedules() {
        List<ScheduleCounselor> schedules= adminMapper.getCounselorSchedules();
        List<ScheduleCounselorResponse> scheduleCounselorResponses=new ArrayList<>();
        for (int i=0;i<schedules.size();i++){
            ScheduleCounselorResponse scheduleCounselorResponse=new ScheduleCounselorResponse();
            BeanUtils.copyProperties(schedules.get(i),scheduleCounselorResponse);
            scheduleCounselorResponse.setCounselorName(visitorMapper.getHistoryCounselor(
                    scheduleCounselorResponse.getCounselorId()
            ).getName());
            scheduleCounselorResponses.add(scheduleCounselorResponse);
        }
        return scheduleCounselorResponses;
    }

    @Override
    public List<ScheduleSupervisorResponse> getSupervisorSchedules() {
        List<ScheduleSupervisor> schedules= adminMapper.getSupervisorSchedules();
        List<ScheduleSupervisorResponse> scheduleSupervisorResponses=new ArrayList<>();
        for (int i=0;i<schedules.size();i++){
            ScheduleSupervisorResponse scheduleSupervisorResponse=new ScheduleSupervisorResponse();
            BeanUtils.copyProperties(schedules.get(i),scheduleSupervisorResponse);
            scheduleSupervisorResponse.setSupervisorName(adminMapper.getSupervisor(
                    scheduleSupervisorResponse.getSupervisorId()
            ).getName());
            scheduleSupervisorResponses.add(scheduleSupervisorResponse);
        }
        return scheduleSupervisorResponses;
    }

    @Override
    public int putCounselorSchedule(ScheduleCounselorRequest scheduleCounselorRequest) {
        return adminMapper.insertScheduleCounselor(scheduleCounselorRequest.getCounselorId(),
                scheduleCounselorRequest.getWeekday());
    }

    @Override
    public int removeCounselorSchedule(ScheduleCounselorRequest scheduleCounselorRequest) {
        return adminMapper.deleteScheduleCounselor(scheduleCounselorRequest.getCounselorId(),
                scheduleCounselorRequest.getWeekday());
    }

    @Override
    public int removeSupervisorSchedule(ScheduleSupervisorRequest scheduleSupervisorRequest) {
        return adminMapper.deleteScheduleSupervisor(scheduleSupervisorRequest.getSupervisorId(),
                scheduleSupervisorRequest.getWeekday());
    }


}
