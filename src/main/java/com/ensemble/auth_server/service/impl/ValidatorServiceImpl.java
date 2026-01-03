package com.ensemble.auth_server.service.impl;

import com.ensemble.auth_server.entity.Role;
import com.ensemble.auth_server.entity.UserAccount;
import com.ensemble.auth_server.enums.EmploymentStatus;
import com.ensemble.auth_server.exception.AlreadyException;
import com.ensemble.auth_server.repo.RoleRepository;
import com.ensemble.auth_server.repo.UserAccountRepository;
import com.ensemble.auth_server.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class ValidatorServiceImpl implements ValidationService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private RoleRepository roleRepository;

    public Optional<UserAccount> checkUserAccountNotRegistered(String email) {
        Optional<UserAccount> existingCustomer = userAccountRepository.findByEmail(email);
        if (existingCustomer.isEmpty()) {
            throw new AlreadyException(email + " is not registered!");
        }
        return existingCustomer;
    }

    public void checkUserAccountAlreadyExit(String email) {
        Optional<UserAccount> existingCustomer = userAccountRepository.findByEmail(email);
        if (existingCustomer.isPresent()) {
            throw new AlreadyException(existingCustomer.get().getRole() + " is already registered with the given email: " + email);
        }
    }

    public Role checkRoleNotRegistered(String role) {
        Role userRoles = roleRepository.findByCode(role);
        if (userRoles == null) {
            throw new AlreadyException("Role is not registered with the given email: " + role);
        }
        return userRoles;
    }


    public Optional<UserAccount> checkSupervisor(String superVisorEmail) {
        Optional<UserAccount> existingManager = checkUserAccountNotRegistered(superVisorEmail);
        if (existingManager.isEmpty()) {
            throw new AlreadyException("Manager is not registered" + " with the given email: " + superVisorEmail);
        } else if (!EmploymentStatus.ACTIVE.toString().equals(existingManager.get().getEmploymentStatus())) {
            throw new AlreadyException("Manager is " + existingManager.get().getEmploymentStatus() + " with the given email: " + superVisorEmail);
        }
        return existingManager;
    }


//    @Override
//    public void checkAPIsExitsOrNotClient(List<RoleApiAccessConfigClient> configs, Set<String> endpoints) {
//        for (String endpoint : endpoints) {
//            configs.stream()
//                    .filter(config -> endpoint.equals(config.getApiEndpoint()))
//                    .findAny()
//                    .ifPresent(config -> {
//                        throw new AlreadyException(
//                                "API already exists:" + config.getApiEndpoint() + " for role: " + config.getUserRolesClient().getName()
//                        );
//                    });
//        }
//    }
//
//    @Override
//    public void checkAPIsExitsOrNotPortal(List<RoleApiAccessConfigPortal> configs, Set<String> endpoints) {
//        for (String endpoint : endpoints) {
//            configs.stream()
//                    .filter(config -> endpoint.equals(config.getApiEndpoint()))
//                    .findAny()
//                    .ifPresent(config -> {
//                        throw new AlreadyException(
//                                "API already exists:" + config.getApiEndpoint() + " for role: " + config.getUserRolesPortal().getName()
//                        );
//                    });
//        }
//    }

}

