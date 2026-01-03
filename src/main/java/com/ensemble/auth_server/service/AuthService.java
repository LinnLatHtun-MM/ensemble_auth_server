package com.ensemble.auth_server.service;

import com.ensemble.auth_server.dto.RegisterRequest;
import com.ensemble.auth_server.entity.UserAccount;

public interface AuthService {

    UserAccount register(RegisterRequest request);

    String login(String email, String password, String userType);

}

