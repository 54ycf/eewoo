package com.eewoo.platform.service;


import com.eewoo.platform.pojo.vo.response.CounselorSupervisorResponse;
import com.eewoo.platform.pojo.vo.response.SessionResponse;


import java.util.List;

public interface AdminService {

    int disableUser(Integer id, String role);

    List<SessionResponse> getSessions();

    List<CounselorSupervisorResponse> getCounselors();

    List<CounselorSupervisorResponse> getSupervisors();

    int removeCounselor(Integer id);


}
