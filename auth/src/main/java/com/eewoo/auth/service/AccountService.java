package com.eewoo.auth.service;

import com.eewoo.common.pojo.*;

public interface AccountService {
    String login(User user);
    void logout();
    void logout(Integer id, String role);

    int registerVisitor(Visitor visitor);
    int addCounselor(Counselor counselor);
    int addSupervisor(Supervisor supervisor);
}
