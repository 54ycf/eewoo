package com.eewoo.chat.service;

import com.eewoo.chat.pojo.CounselorComment;
import com.eewoo.chat.pojo.VisitorComment;

import com.eewoo.chat.pojo.Chat;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


public interface ChatService {

    boolean callCounselor(Integer counselorId, String counselorName);

    void endVCSession(String chatToken);

    void visitorComment(VisitorComment visitorComment);

    void counselorComment(CounselorComment counselorComment);

    boolean callSupervisor(String chatToken);

    void endSCSession(String chatToken);

    Integer getChatsNum();

    List<Integer> getOnlineCounselors();

    void getSessionInMongo(Integer sessionId, HttpServletResponse response);

    void getSessionsInMongo(List<Integer> sessionIds, HttpServletResponse response);

    Chat getSessionContent(Integer sessionId);
}
