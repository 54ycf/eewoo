package com.eewoo.platform.service.impl;

import com.eewoo.common.pojo.Counselor;
import com.eewoo.common.util.R;
import com.eewoo.platform.mapper.AdminMapper;
import com.eewoo.platform.pojo.model.Session;
import com.eewoo.common.pojo.User;
import com.eewoo.common.pojo.Visitor;
import com.eewoo.common.pojo.vo.request.SessionRequest;
import com.eewoo.common.security.LoginUser;
import com.eewoo.platform.mapper.VisitorMapper;
import com.eewoo.platform.pojo.vo.response.CounselorResponse;
import com.eewoo.platform.pojo.vo.response.SessionResponse;
import com.eewoo.platform.pojo.vo.response.VisitorResponse;
import com.eewoo.platform.service.VisitorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.eewoo.platform.feign.ChatFeign;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.*;

@Service
public class VisitorServiceImpl implements VisitorService {
    @Autowired
    VisitorMapper visitorMapper;

    @Autowired
    AdminMapper adminMapper;

    @Autowired
    ChatFeign chatFeign;

    String getToken(){
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getHeader("token");
    }


    /**获取访客信息**/
    @Override
    public VisitorResponse getInfo() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        Visitor info = visitorMapper.getInfo(id);
        VisitorResponse visitorResponse = new VisitorResponse();
        BeanUtils.copyProperties(info, visitorResponse);
        return visitorResponse;
    }

    /**获取所有咨询师**/
    @Override
    public List<CounselorResponse> getCounselors() {
        List<Counselor> counselors= visitorMapper.getCounselors();
        List<CounselorResponse> counselorResponses=new ArrayList<>();
        Counselor counselor=null;
        R<List<Integer>> r = chatFeign.getOnlineCounselorIds(getToken());
        if( r == null)
            throw new RuntimeException("Remote call failed");
        List<Integer>  list1 = r.getData();
        for (int i=0;i<counselors.size();i++){
            CounselorResponse counselorResponse=new CounselorResponse();
            counselor=counselors.get(i);
            BeanUtils.copyProperties(counselor,counselorResponse);
            counselorResponse.setSessionCount(adminMapper.selectSessionCountById(counselor.getId()));
            counselorResponse.setSessionScore(adminMapper.countAvgSessionScoreById(counselor.getId()));
            if(list1.contains(counselor.getId())){
                counselorResponse.setStatus(1);
            }

            counselorResponses.add(counselorResponse);
        }
        return counselorResponses;
    }

    /**获取历史咨询师**/
    @Override
    public List<CounselorResponse> getHistoryCounselors() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        List<SessionResponse> sessionResponses=visitorMapper.getSessions(id);
        Set<CounselorResponse> counselorResponses=new HashSet<>();
        R r = chatFeign.getOnlineCounselorIds(getToken());
        if( r == null)
            throw new RuntimeException("Remote call failed");
        List  list1 = (List)r.getData();
        for (int i=0;i<sessionResponses.size();i++){
            Counselor counselor= visitorMapper.getHistoryCounselor(sessionResponses.get(i).getCounselorId());
            CounselorResponse counselorResponse=new CounselorResponse();
            BeanUtils.copyProperties(counselor,counselorResponse);
            counselorResponse.setSessionCount(adminMapper.selectSessionCountById(counselor.getId()));
            counselorResponse.setSessionScore(adminMapper.countAvgSessionScoreById(counselor.getId()));
            if(list1.contains(counselor.getId())){
                counselorResponse.setStatus(1);
            }
            counselorResponses.add(counselorResponse);
        }
        return new ArrayList<>(counselorResponses);
    }

    /**给咨询师评价**/
    @Override
    public void giveCounselorComment(Integer sessionId, String feedback, Integer score) {
        visitorMapper.insertComment(sessionId,feedback,score);
        visitorMapper.endSession(sessionId, new Date());
        return;
    }

    /**创建会话**/
    @Override
    public int createSession(SessionRequest sessionVo) {
        Session session = new Session();
        BeanUtils.copyProperties(sessionVo, session);
        int ok= visitorMapper.insertSession(session);
        if(ok==1){
            return session.getId();
        }
        return -1;

    }

    /**获取历史会话**/
    @Override
    public List<SessionResponse> getHistorySessions() {
        User user = ((LoginUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Integer id = user.getId();
        List<Session> sessions=visitorMapper.getHistorySessions(id);
        List<SessionResponse> sessionResponses=new ArrayList<>();
        for (int i=0;i<sessions.size();i++){
            SessionResponse sessionResponse=new SessionResponse();
            Session session=sessions.get(i);
            BeanUtils.copyProperties(session,sessionResponse);
            sessionResponse.setVisitorName(visitorMapper.getInfo(session.getVisitorId()).getName());
            sessionResponse.setCounselorName(visitorMapper.getHistoryCounselor(session.getCounselorId()).getName());
            sessionResponse.setProfile(visitorMapper.getHistoryCounselor(session.getCounselorId()).getProfile());
            sessionResponses.add(sessionResponse);

        }

        return sessionResponses;
    }



}
