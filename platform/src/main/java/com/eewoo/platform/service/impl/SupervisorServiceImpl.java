package com.eewoo.platform.service.impl;
import com.eewoo.platform.mapper.SupervisorMapper;
import com.eewoo.platform.pojo.vo.response.CounselorResponse;
import com.eewoo.platform.pojo.vo.response.VisitorResponse;
import com.eewoo.platform.service.SupervisorService;
import org.springframework.beans.factory.annotation.Autowired;
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


}
