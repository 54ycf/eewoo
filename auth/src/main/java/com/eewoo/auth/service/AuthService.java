package com.eewoo.auth.service;

import com.eewoo.common.security.LoginUser;

public interface AuthService {
    LoginUser parse(String token);
}
