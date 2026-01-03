package com.ensemble.auth_server.repo;

import com.ensemble.auth_server.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByCode(String code);
}

