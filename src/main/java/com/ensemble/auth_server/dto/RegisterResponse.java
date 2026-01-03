package com.ensemble.auth_server.dto;


public record RegisterResponse(String message, Long id, String username, String userType, String role) {
}