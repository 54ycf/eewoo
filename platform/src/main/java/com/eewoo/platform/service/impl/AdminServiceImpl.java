package com.eewoo.platform.service.impl;

import com.eewoo.common.pojo.*;
import com.eewoo.common.util.Constant;
import com.eewoo.platform.mapper.AdminMapper;
import com.eewoo.platform.mapper.UserMapper;
import com.eewoo.platform.mapper.VisitorMapper;
import com.eewoo.platform.pojo.vo.request.*;
import com.eewoo.platform.pojo.vo.response.*;
import com.eewoo.platform.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    VisitorMapper visitorMapper;

    /**
     * 禁用用户
     * **/
    @Override
    public int disableUser(Integer id, String role) {
        return userMapper.disableUser(id, Constant.roleTableMap.get(role));
    }
    @Override
    public int enableUser(Integer id, String role) {
        return userMapper.enableUser(id, Constant.roleTableMap.get(role));
    }

    /**
     * 获取咨询记录
     * **/
    @Override
    public PageInfo<SessionResponse> getSessions(Integer page,Integer pageSize) {
        PageHelper.startPage(page, pageSize);
        List<SessionResponse> sessionResponses = adminMapper.getSessions();
        PageInfo<SessionResponse> pageInfo = new PageInfo<>(sessionResponses);
        return pageInfo;
    }





    /**
     * 获取咨询师列表(含绑定的督导)
     * **/
    @Override
    public List<CounselorSupervisorResponse> getCounselors(Integer page,Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<CounselorSupervisorResponse> counselorSupervisorResponses=adminMapper.getCounselorsAndSupervisors();
        PageInfo<CounselorSupervisorResponse> pageInfo=new PageInfo<>(counselorSupervisorResponses);
        return pageInfo.getList();
    }

    /**
     * 获取督导列表(含绑定的咨询师)
     * **/
    @Override
    public List<CounselorSupervisorResponse> getSupervisors(Integer page,Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<CounselorSupervisorResponse> counselorSupervisorResponses= adminMapper.getCounselorsAndSupervisors();
        PageInfo<CounselorSupervisorResponse> pageInfo=new PageInfo<>(counselorSupervisorResponses);
        return pageInfo.getList();
    }

    /**删除咨询师**/
    @Override
    public int removeCounselor(Integer id) {
        int ok=adminMapper.deleteCounselor(id);
        return ok;
    }


    /**得到访客**/
    @Override
    public PageInfo<VisitorResponse> getVistors(Integer page,Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        List<Visitor> visitors= adminMapper.getVisitors();
        List<VisitorResponse> visitorResponses=new ArrayList<>();
        for (int i=0;i<visitors.size();i++){
            VisitorResponse visitorResponse=new VisitorResponse();
            BeanUtils.copyProperties(visitors.get(i),visitorResponse);
            visitorResponses.add(visitorResponse);
        }
        PageInfo<VisitorResponse> pageInfo=new PageInfo<>(visitorResponses);
        return pageInfo;
    }

    /**得到会话数量最高的咨询师**/
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

    /**得到得分最高的咨询师**/
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

    /**获取咨询师排班表(按星期)**/
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

    /**获取督导排班表(按星期)**/
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

    /**添加咨询师排班(按星期)**/
    @Override
    public int putCounselorSchedule(ScheduleCounselorRequest scheduleCounselorRequest) {
        ScheduleCounselorResponse scheduleCounselorResponse= adminMapper.getScheduleCounselor(scheduleCounselorRequest.getCounselorId(),
                scheduleCounselorRequest.getWeekday());
        if(scheduleCounselorResponse!=null){
            return -1;
        }
        return adminMapper.insertScheduleCounselor(scheduleCounselorRequest.getCounselorId(),
                scheduleCounselorRequest.getWeekday());
    }

    /**删除咨询师排班(按星期)**/
    @Override
    public int removeCounselorSchedule(ScheduleCounselorRequest scheduleCounselorRequest) {
        return adminMapper.deleteScheduleCounselor(scheduleCounselorRequest.getCounselorId(),
                scheduleCounselorRequest.getWeekday());
    }

    /**删除督导排班(按星期)**/
    @Override
    public int removeSupervisorSchedule(ScheduleSupervisorRequest scheduleSupervisorRequest) {
        return adminMapper.deleteScheduleSupervisor(scheduleSupervisorRequest.getSupervisorId(),
                scheduleSupervisorRequest.getWeekday());
    }

    /**获取咨询师排班表(按日期)**/
    @Override
    public List<DayScheduleCounselorResponse> getCounselorSchedulesByDay() {
        List<DayScheduleCounselorResponse> dayScheduleCounselorResponses=adminMapper.getCounselorSchedulesByDay();
        DayScheduleCounselorResponse dayScheduleCounselorResponse=null;
        for (int i=0;i<dayScheduleCounselorResponses.size();i++){
            dayScheduleCounselorResponse=dayScheduleCounselorResponses.get(i);
            dayScheduleCounselorResponse.setCounselorName(adminMapper.selectCounselorById(
                    dayScheduleCounselorResponse.getCounselorId()
            ).getName());
        }
        return dayScheduleCounselorResponses;

    }

    @Override
    public List<DayScheduleSupervisorResponse> getSupervisorSchedulesByDay() {
        List<DayScheduleSupervisorResponse> dayScheduleSupervisorResponses=adminMapper.getSupervisorSchedulesByDay();
        DayScheduleSupervisorResponse dayScheduleSupervisorResponse=null;
        for (int i=0;i<dayScheduleSupervisorResponses.size();i++){
            dayScheduleSupervisorResponse=dayScheduleSupervisorResponses.get(i);
            dayScheduleSupervisorResponse.setSupervisorName(adminMapper.selectSuperviNameById(
                    dayScheduleSupervisorResponse.getSupervisorId()
            ));
        }
        return dayScheduleSupervisorResponses;
    }

    @Override
    public int putCounselorScheduleByDay(DayScheduleCounselorRequest dayScheduleCounselorRequest) {
        DayScheduleCounselorResponse dayScheduleCounselorResponse= adminMapper.selectDayScheduleCounselor(
                dayScheduleCounselorRequest.getCounselorId(),
                dayScheduleCounselorRequest.getDay()
        );
        /**获取该日期对应的星期几**/
        Date date=dayScheduleCounselorRequest.getDay();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        Integer weekday=calendar.get(Calendar.DAY_OF_WEEK);
        /**获取该counselor的排班星期**/
        List<Integer> schedule=adminMapper.getCounselorScheduleById(dayScheduleCounselorRequest.getCounselorId());
        /**看星期中是否包含了该日期**/
        if(schedule.contains(weekday)) {
            return -1;
        }
        if(dayScheduleCounselorResponse==null){
            return adminMapper.insertScheduleCounselorByDay(dayScheduleCounselorRequest.getCounselorId(),
                    dayScheduleCounselorRequest.getDay());
        }
        return adminMapper.updateScheduleCounselorByDay(
                dayScheduleCounselorRequest.getCounselorId(),
                dayScheduleCounselorRequest.getDay()
        );
    }

    @Override
    public int deleteCounselorScheduleByDay(DayScheduleCounselorRequest dayScheduleCounselorRequest) {
        DayScheduleCounselorResponse dayScheduleCounselorResponse=adminMapper.selectDayScheduleCounselor(
                dayScheduleCounselorRequest.getCounselorId(),
                dayScheduleCounselorRequest.getDay()
        );
        if(dayScheduleCounselorResponse==null){
            /**获取该日期对应的星期几**/
            Date date=dayScheduleCounselorRequest.getDay();
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            Integer weekday=calendar.get(Calendar.DAY_OF_WEEK);
            /**获取该counselor的排班星期**/
            List<Integer> schedule=adminMapper.getCounselorScheduleById(dayScheduleCounselorRequest.getCounselorId());
            /**看星期中是否包含了该日期**/
            if(schedule.contains(weekday)){
                /**如果包含了先增加再删除**/
                adminMapper.insertScheduleCounselorByDay(dayScheduleCounselorRequest.getCounselorId(),
                        dayScheduleCounselorRequest.getDay());
                adminMapper.deleteScheduleCounselorByDay(dayScheduleCounselorRequest.getCounselorId(),
                        dayScheduleCounselorRequest.getDay());
                return 1;
            }else {
                return -1;
            }
        }
        return adminMapper.deleteScheduleCounselorByDay(
                dayScheduleCounselorRequest.getCounselorId(),
                dayScheduleCounselorRequest.getDay()
        );
    }

    @Override
    public int putSupervisorScheduleByDay(DayScheduleSupervisorRequest dayScheduleSupervisorRequest) {
        DayScheduleSupervisorResponse dayScheduleSupervisorResponse= adminMapper.selectDayScheduleSupervisor(
                dayScheduleSupervisorRequest.getSupervisorId(),
                dayScheduleSupervisorRequest.getDay()
        );
        Date date=dayScheduleSupervisorRequest.getDay();
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        Integer weekday=calendar.get(Calendar.DAY_OF_WEEK);
        List<Integer> schedule=adminMapper.getSupervisorScheduleById(dayScheduleSupervisorRequest.getSupervisorId());
        if(schedule.contains(weekday)){
            return -1;
        }
        if(dayScheduleSupervisorResponse==null){
            return adminMapper.insertScheduleSupervisorByDay(
                    dayScheduleSupervisorRequest.getSupervisorId(),
                    dayScheduleSupervisorRequest.getDay()
            );
        }
        return adminMapper.updateScheduleSupervisorByDay(
                dayScheduleSupervisorRequest.getSupervisorId(),
                dayScheduleSupervisorRequest.getDay()
        );
    }

    @Override
    public int deleteSupervisorScheduleByDay(DayScheduleSupervisorRequest dayScheduleSupervisorRequest) {
        DayScheduleSupervisorResponse dayScheduleSupervisorResponse= adminMapper.selectDayScheduleSupervisor(
                dayScheduleSupervisorRequest.getSupervisorId(),
                dayScheduleSupervisorRequest.getDay()
        );
        if(dayScheduleSupervisorResponse==null){
            Date date=dayScheduleSupervisorRequest.getDay();
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            Integer weekday=calendar.get(Calendar.DAY_OF_WEEK);
            List<Integer> schedule=adminMapper.getSupervisorScheduleById(dayScheduleSupervisorRequest.getSupervisorId());
            if(schedule.contains(weekday)){
                adminMapper.insertScheduleSupervisorByDay(dayScheduleSupervisorRequest.getSupervisorId(),
                        dayScheduleSupervisorRequest.getDay());
                adminMapper.deleteScheduleSupervisorByDay(dayScheduleSupervisorRequest.getSupervisorId(),
                        dayScheduleSupervisorRequest.getDay());
                return 1;
            }else {
                return -1;
            }
        }
        return adminMapper.deleteScheduleSupervisorByDay(
                dayScheduleSupervisorRequest.getSupervisorId(),
                dayScheduleSupervisorRequest.getDay()
        );
    }


    @Override
    public CounselorResponse getCounselorById(Integer counselorId) {
        CounselorResponse counselorResponse=new CounselorResponse();
        Counselor counselor= visitorMapper.getHistoryCounselor(counselorId);
        BeanUtils.copyProperties(counselor,counselorResponse);
        return counselorResponse;
    }

    @Override
    public PageInfo<AdminCounselorResponse> getCounselorsWithoutSupervi(Integer page, Integer pageSize) {

        List<Counselor> counselors= visitorMapper.getCounselors();
        System.out.println("counselors: "+counselors);
        List<AdminCounselorResponse> adminCounselorResponses=new ArrayList<>();
        Counselor counselor=null;
        for (int i=0;i<counselors.size();i++){
            AdminCounselorResponse adminCounselorResponse=new AdminCounselorResponse();
            counselor=counselors.get(i);
            Integer supervisorId=adminMapper.selectSuperviIdByBind(counselor.getId());
            adminCounselorResponse.setCounselorId(counselor.getId());
            adminCounselorResponse.setName(counselor.getName());
            adminCounselorResponse.setSupervisorId(supervisorId);
            adminCounselorResponse.setSupervisor(adminMapper.selectSuperviNameById(supervisorId));
            adminCounselorResponse.setSessionCount(adminMapper.selectSessionCountById(counselor.getId()));
            adminCounselorResponse.setSessionTime(adminMapper.selectSessionTimeById(counselor.getId()));
            adminCounselorResponse.setSessionScore(adminMapper.countAvgSessionScoreById(counselor.getId()));
            adminCounselorResponse.setSchedule(adminMapper.getCounselorScheduleById(counselor.getId()));
            adminCounselorResponse.setBanned(counselor.getBanned());
            adminCounselorResponse.setProfile(counselor.getProfile());
            adminCounselorResponses.add(adminCounselorResponse);
        }
        System.out.println("null: "+adminCounselorResponses);
        PageHelper.startPage(page,pageSize);
        PageInfo<AdminCounselorResponse> pageInfo=new PageInfo<>(adminCounselorResponses);
        return pageInfo;
    }

    @Override
    public PageInfo<AdminCounselorResponse> getCounselorByName(String name, Integer page,Integer pageSize) {
        List<Counselor> counselors=adminMapper.selectCounselorsByName(name);
        List<AdminCounselorResponse> adminCounselorResponses=new ArrayList<>();
        Counselor counselor=null;
        for (int i=0;i<counselors.size();i++){
            AdminCounselorResponse adminCounselorResponse=new AdminCounselorResponse();
            counselor=counselors.get(i);
            int supervisorId=adminMapper.selectSuperviIdByBind(counselor.getId());
            adminCounselorResponse.setName(counselor.getName());
            adminCounselorResponse.setCounselorId(counselor.getId());
            adminCounselorResponse.setSupervisorId(supervisorId);
            adminCounselorResponse.setSupervisor(adminMapper.selectSuperviNameById(supervisorId));
            adminCounselorResponse.setSessionCount(adminMapper.selectSessionCountById(counselor.getId()));
            adminCounselorResponse.setSessionTime(adminMapper.selectSessionTimeById(counselor.getId()));
            adminCounselorResponse.setSessionScore(adminMapper.countAvgSessionScoreById(counselor.getId()));
            adminCounselorResponse.setSchedule(adminMapper.getCounselorScheduleById(counselor.getId()));
            adminCounselorResponse.setBanned(counselor.getBanned());
            adminCounselorResponse.setProfile(counselor.getProfile());
            adminCounselorResponses.add(adminCounselorResponse);
        }
        PageHelper.startPage(page,pageSize);
        PageInfo<AdminCounselorResponse> pageInfo=new PageInfo<>(adminCounselorResponses);
        return pageInfo;
    }

    @Override
    public PageInfo<AdminSupervisorResponse> getSupervisorsWithoutCounsel(Integer page, Integer pageSize) {
        List<Supervisor> supervisors=adminMapper.getSupervisors();
        List<AdminSupervisorResponse> adminSupervisorResponses=new ArrayList<>();
        Supervisor supervisor=null;
        for (int i=0;i<supervisors.size();i++){
            AdminSupervisorResponse adminSupervisorResponse=new AdminSupervisorResponse();
            supervisor=supervisors.get(i);
            List<Integer> counselorIds=adminMapper.selectCounselorIdByBind(supervisor.getId());
            List<Counselor> counselors=new ArrayList<>();
            for (int j=0;j<counselorIds.size();j++){
                Counselor counselor=adminMapper.selectCounselorById(counselorIds.get(j));
                counselors.add(counselor);
            }
            adminSupervisorResponse.setSupervisorId(supervisor.getId());
            adminSupervisorResponse.setName(supervisor.getName());
            adminSupervisorResponse.setCounselors(counselors);
            adminSupervisorResponse.setSchedule(adminMapper.getSupervisorScheduleById(supervisor.getId()));
            adminSupervisorResponse.setBanned(supervisor.getBanned());
            adminSupervisorResponse.setAge(supervisor.getAge());
            adminSupervisorResponse.setIdCard(supervisor.getIdCard());
            adminSupervisorResponse.setPhone(supervisor.getPhone());
            adminSupervisorResponse.setEmail(supervisor.getEmail());
            adminSupervisorResponse.setWorkPlace(supervisor.getWorkPlace());
            adminSupervisorResponse.setQualification(supervisor.getQualification());
            adminSupervisorResponse.setQualificationNumber(supervisor.getQualificationNumber());
            adminSupervisorResponse.setProfile(supervisor.getProfile());
            adminSupervisorResponse.setTitle(supervisor.getTitle());
            adminSupervisorResponses.add(adminSupervisorResponse);
        }
        PageHelper.startPage(page,pageSize);
        PageInfo<AdminSupervisorResponse> pageInfo=new PageInfo<>(adminSupervisorResponses);
        return pageInfo;
    }

    @Override
    public PageInfo<AdminSupervisorResponse> getSupervisorByName(String name,Integer page,Integer pageSize) {
        List<Supervisor> supervisors=adminMapper.selectSupervisorsByName(name);
        List<AdminSupervisorResponse> adminSupervisorResponses=new ArrayList<>();
        Supervisor supervisor=null;
        for (int i=0;i<supervisors.size();i++){
            AdminSupervisorResponse adminSupervisorResponse=new AdminSupervisorResponse();
            supervisor=supervisors.get(i);
            List<Integer> counselorIds=adminMapper.selectCounselorIdByBind(supervisor.getId());
            List<Counselor> counselors=new ArrayList<>();
            for (int j=0;j<counselorIds.size();j++){
                Counselor counselor=adminMapper.selectCounselorById(counselorIds.get(j));
                counselors.add(counselor);
            }
            adminSupervisorResponse.setSupervisorId(supervisor.getId());
            adminSupervisorResponse.setName(supervisor.getName());
            adminSupervisorResponse.setCounselors(counselors);
            adminSupervisorResponse.setSchedule(adminMapper.getSupervisorScheduleById(supervisor.getId()));
            adminSupervisorResponse.setBanned(supervisor.getBanned());
            adminSupervisorResponse.setAge(supervisor.getAge());
            adminSupervisorResponse.setIdCard(supervisor.getIdCard());
            adminSupervisorResponse.setPhone(supervisor.getPhone());
            adminSupervisorResponse.setEmail(supervisor.getEmail());
            adminSupervisorResponse.setWorkPlace(supervisor.getWorkPlace());
            adminSupervisorResponse.setQualification(supervisor.getQualification());
            adminSupervisorResponse.setQualificationNumber(supervisor.getQualificationNumber());
            adminSupervisorResponse.setProfile(supervisor.getProfile());
            adminSupervisorResponse.setTitle(supervisor.getTitle());
            adminSupervisorResponses.add(adminSupervisorResponse);

        }
        PageHelper.startPage(page,pageSize);
        PageInfo<AdminSupervisorResponse> pageInfo=new PageInfo<>(adminSupervisorResponses);
        return pageInfo;

    }

    @Override
    public int reviseBind(BindRequest bindRequest) {
        return adminMapper.updateBind(bindRequest.getCounselorId(),
                bindRequest.getSupervisorId());
    }

    @Override
    public PageInfo<AdminCounselorResponse> getCounselorList(Integer page, Integer size, String name) {
        PageHelper.startPage(page, size);
        List<AdminCounselorResponse> counselorList  = adminMapper.getCounselorList(name);
        PageInfo<AdminCounselorResponse> result = new PageInfo<>(counselorList);
        result.getList().forEach(item -> {
            if (item.getScheduleListStr() != null) {
                item.setSchedule(Arrays.stream(item.getScheduleListStr().split(",")).map(Integer::parseInt).collect(Collectors.toList()));
            } else {
                item.setSchedule(new ArrayList<>());
            }
        });
        return result;
    }

    @Override
    public PageInfo<AdminSupervisorResponse> getSupervisorList(Integer page, Integer size, String name) {
        PageHelper.startPage(page,size);
        List<AdminSupervisorResponse> supervisorList= adminMapper.getSupervisorList(name);
        PageInfo<AdminSupervisorResponse> result=new PageInfo<>(supervisorList);
        for (AdminSupervisorResponse adminSupervisorResponse:result.getList()){
            adminSupervisorResponse.setCounselors(adminMapper.getCounselorsByBind(adminSupervisorResponse.getSupervisorId()));
            adminSupervisorResponse.setSchedule(adminMapper.getSupervisorScheduleById(adminSupervisorResponse.getSupervisorId()));
        }
        return result;
    }


}
