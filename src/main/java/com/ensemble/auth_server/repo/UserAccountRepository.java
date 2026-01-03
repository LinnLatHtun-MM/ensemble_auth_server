package com.ensemble.auth_server.repo;

import com.ensemble.auth_server.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {


    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findByEmailAndUserType(String username, String userType);

    boolean existsByEmailAndUserType(String username, String userType);
}
