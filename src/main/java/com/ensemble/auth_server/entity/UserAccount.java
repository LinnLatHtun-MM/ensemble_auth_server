package com.ensemble.auth_server.entity;

import com.ensemble.auth_server.enums.EmploymentStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_account", uniqueConstraints = @UniqueConstraint(columnNames = {"userName", "userType"}))
@Getter
@Setter
public class UserAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(nullable = false, length = 100)
    private String userName;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 255)
    private String password;

    @Column(length = 100)
    private String supervisor;

    @Column(nullable = false,length = 100)
    private EmploymentStatus employmentStatus = EmploymentStatus.PROBATION;

    @Column(nullable = false, length = 20)
    private String userType; // PORTAL / CLIENT

    @Column(nullable = false, length = 50)
    private String role; // SUPER_ADMIN / HR / MANAGER

    @Column(nullable = false)
    private boolean enabled = true;
}

