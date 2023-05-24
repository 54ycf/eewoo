package com.eewoo.platform.service;

import com.eewoo.platform.pojo.vo.request.DisableUserRequest;

public interface AdminService {

    int disableUser(Integer id, String role);

}
