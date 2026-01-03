package com.ensemble.auth_server.service.impl;

import com.ensemble.auth_server.dto.RegisterRequest;
import com.ensemble.auth_server.entity.Role;
import com.ensemble.auth_server.entity.UserAccount;
import com.ensemble.auth_server.exception.ResourceNotFoundException;
import com.ensemble.auth_server.repo.UserAccountRepository;
import com.ensemble.auth_server.service.AuthService;
import com.ensemble.auth_server.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private ValidationService validationService;

    private final String ADMIN_ROLE = "ADMIN";
    private final String SUPER_ADMIN_ROLE = "SUPER_ADMIN";

    public UserAccount register(RegisterRequest request) {
        validationService.checkUserAccountAlreadyExit(request.email());

        Role existingRole = validationService.checkRoleNotRegistered(request.role());
        String role = existingRole.getCode();
        UserAccount newUser = new UserAccount();
        if (!role.equals(ADMIN_ROLE) && !role.equals(SUPER_ADMIN_ROLE)) {
            Optional<UserAccount> supervisor = validationService.checkSupervisor(request.supervisor());
            newUser.setSupervisor(supervisor.get().getEmail());
        }
        newUser.setUserName(request.userName());
        newUser.setEmail(request.email());
        newUser.setPassword(encoder.encode(request.password()));
        newUser.setUserType(request.userType().toUpperCase());
        newUser.setRole(request.role());
        newUser.setEnabled(true);
        return userAccountRepository.save(newUser);
    }

    public String login(String email, String password, String userType) {

        UserAccount existingUser = userAccountRepository.findByEmailAndUserType(email, userType)
                .orElseThrow(() -> new ResourceNotFoundException(email));

        if (!existingUser.isEnabled() || !encoder.matches(password, existingUser.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("auth-service")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(3600))
                .subject(existingUser.getEmail())
                .claim("userType", existingUser.getUserType())
                .claim("role", existingUser.getRole())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }
}

//package com.ensemble.auth_server.jwt;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.jwt.*;
//        import org.springframework.stereotype.Service;
//
//import java.time.Instant;

//@Service
//@RequiredArgsConstructor
//public class TokenService {
//
//    private final JwtEncoder jwtEncoder;
//
//    public String generate(String username, String role) {
//        Instant now = Instant.now();
//
//        JwtClaimsSet claims = JwtClaimsSet.builder()
//                .issuer("ensemble-auth")
//                .issuedAt(now)
//                .expiresAt(now.plusSeconds(3600))
//                .subject(username)
//                .claim("role", role) // ✅ gateway/hr will read this
//                .build();
//
//        JwsHeader headers = JwsHeader.with(SignatureAlgorithm.RS256).build();
//        return jwtEncoder.encode(JwtEncoderParameters.from(headers, claims)).getTokenValue();
//    }
//}

