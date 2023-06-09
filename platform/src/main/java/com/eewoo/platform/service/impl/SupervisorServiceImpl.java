package com.eewoo.platform.service.impl;
import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.pojo.Supervisor;
import com.eewoo.common.pojo.User;
import com.eewoo.common.security.LoginUser;
import com.eewoo.platform.mapper.SupervisorMapper;
import com.eewoo.platform.pojo.RoughCouselor;
import com.eewoo.platform.pojo.vo.request.FindCounselorMsg;
import com.eewoo.platform.pojo.vo.response.BindCounselorResponse;
import com.eewoo.platform.pojo.vo.response.CounselorResponse;
import com.eewoo.platform.pojo.vo.response.VisitorResponse;
import com.eewoo.platform.service.SupervisorService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SupervisorServiceImpl implements SupervisorService {
    @Autowired
    SupervisorMapper mapper;
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

    @Override
    public List<BindCounselorResponse> bindCounselorsList(Integer page, Integer size) {

        /*
        伪返回，返回所有咨询师的列表；
         */
       // String superVisorname =mapper.getName(id);
//
//        for (BindCounselorResponse bindCounselorResponse : list) {
//            bindCounselorResponse.setSupName(superVisorname);
//        }
        PageHelper.startPage(page,size);
        List<BindCounselorResponse> list = mapper.getbindcounselors();
        PageInfo<BindCounselorResponse> pageInfo = new PageInfo<>(list);
        return pageInfo.getList();
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
            return mapper.getCounselorById(Integer.parseInt(fcm.getIdCard()));
        if(fcm.getUsername() != null)
            return mapper.getConsounselorsByUsername(fcm.getUsername());
        if(fcm.getName() != null)
            return mapper.getConsounselorsByName(fcm.getName());

        return null;
    }


}
