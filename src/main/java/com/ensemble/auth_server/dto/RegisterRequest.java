package com.ensemble.auth_server.dto;

public record RegisterRequest(String userName, String email, String password, String userType, String role,
                              String supervisor) {
}