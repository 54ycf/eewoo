package com.eewoo.platform.service.impl;
import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.common.pojo.User;
import com.eewoo.common.security.LoginUser;
import com.eewoo.common.util.R;
import com.eewoo.platform.feign.ChatFeign;
import com.eewoo.platform.mapper.SupervisorMapper;
import com.eewoo.platform.pojo.RoughCouselor;
import com.eewoo.platform.pojo.vo.request.FindCounselorMsg;
import com.eewoo.platform.pojo.vo.response.*;
import com.eewoo.platform.service.SupervisorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
@Service
public class SupervisorServiceImpl implements SupervisorService {
    @Autowired
    SupervisorMapper mapper;

    @Autowired
    ChatFeign chatFeign;

    String getToken(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("token");
    }

    @Override
    public List<VisitorResponse> getVisitors() {
        return mapper.getVisitors();
    }

    @Override
    public List<CounselorResponse> getCounselors() {

         return mapper.getCounselors();
    }

    @Override
    public List<VisitorResponse> getVisitorList() {

        return mapper.getVisitors();
    }

    /**
伪返回，返回所有咨询师的列表；
 */
    // String superVisorname =mapper.getName(id);
//
//        for (BindCounselorResponse bindCounselorResponse : list) {
//            bindCounselorResponse.setSupName(superVisorname);
//        }


    @Override
    public PageInfo<BindCounselorResponse> bindCounselorsList(Integer page, Integer size) {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer sup_id = user.getId();
        R r = chatFeign.getOnlineCounselorIds(getToken());
        if( r == null)
            throw new RuntimeException("Remote call failed");
        List  list1 = (List)r.getData();
//      List<Integer> list1 = new ArrayList<Integer>();
//      list1.add(1);
//      list1.add(2);
      List<BindCounselorResponse> list2 = mapper.getBindcounselors(sup_id);

        for (BindCounselorResponse bindCounselorResponse : list2)
        {
            if (list1.contains(bindCounselorResponse.getCounselorId()))
                bindCounselorResponse.setStatus("online");
        }

        //according to all online counselors to check whether the binding ones are online and assign values
        //default status are offline
        PageHelper.startPage(page,size);
        List<BindCounselorResponse> list = list2;
        PageInfo<BindCounselorResponse> pageInfo = new PageInfo<>(list);
        return pageInfo;
        //达到分页的效果，传递督导的名字不在这里做功能
    }

    @Override
    public List<RoughCouselor> getLatelyChatCounselors() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        return mapper.latelyChatCounselors(id);
    }

    @Override
    public Supervisor getSupervisorInfo() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();

        return mapper.getSupervisorInfo(id);
    }

    @Override
    public List<Counselor> findCounselors(FindCounselorMsg fcm) {

        if(fcm.getIdCard() != null)
            return mapper.getCounselorById((fcm.getIdCard()));
        if(fcm.getUsername() != null)
            return mapper.getConsounselorsByUsername(fcm.getUsername());
        if(fcm.getName() != null)
            return mapper.getConsounselorsByName(fcm.getName());

        return null;
    }

    @Override
    public List<DayScheduleSupervisorResponse> getSuperVisorSchedulesByDay() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        return mapper.getSupervisorSchedulesByDay(id);
    }

    @Override
    public List<ScheduleSupervisorResponse> getSupervisorSchedules() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();

        List<ScheduleSupervisorResponse> schedule = mapper.getSupervisorScheduleByWeek(id);

        return schedule;

    }


}
