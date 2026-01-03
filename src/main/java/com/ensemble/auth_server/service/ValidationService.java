package com.ensemble.auth_server.service;

import com.ensemble.auth_server.entity.Role;
import com.ensemble.auth_server.entity.UserAccount;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ValidationService {

    Optional<UserAccount> checkUserAccountNotRegistered(String email);

    void checkUserAccountAlreadyExit(String email);

    Role checkRoleNotRegistered(String role);

    Optional<UserAccount> checkSupervisor(String supervisorEmail);

//    void checkAPIsExitsOrNotClient(List<RoleApiAccessConfigClient> configs, Set<String> apiEndpoints);

//    void checkAPIsExitsOrNotPortal(List<RoleApiAccessConfigPortal> configs, Set<String> apiEndpoints);


}
