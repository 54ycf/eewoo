package com.eewoo.chat.service;

import com.eewoo.chat.pojo.CounselorComment;
import com.eewoo.chat.pojo.VisitorComment;

import com.eewoo.chat.pojo.Chat;


public interface ChatService {

    boolean callCounselor(Integer counselorId, String counselorName);

    void endVCSession(String chatToken);

    void visitorComment(VisitorComment visitorComment);

    void counselorComment(CounselorComment counselorComment);

    boolean callSupervisor(String chatToken);

    void endCSSession(String chatToken);

}
