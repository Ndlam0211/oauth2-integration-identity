package com.lamnd.zerotohero.service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.lamnd.zerotohero.config.OutboundConfig;
import com.lamnd.zerotohero.dto.request.*;
import com.lamnd.zerotohero.entity.Role;
import com.lamnd.zerotohero.entity.User;
import com.lamnd.zerotohero.enums.ERole;
import com.lamnd.zerotohero.feignclient.OutboundIdentityClient;
import com.lamnd.zerotohero.feignclient.OutboundUserClient;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import com.lamnd.zerotohero.dto.reponse.AuthResponse;
import com.lamnd.zerotohero.dto.reponse.IntrospectResponse;
import com.lamnd.zerotohero.entity.BlacklistToken;
import com.lamnd.zerotohero.exception.AppException;
import com.lamnd.zerotohero.exception.ErrorCode;
import com.lamnd.zerotohero.repository.BlacklistTokenRepo;
import com.lamnd.zerotohero.repository.UserRepo;
import com.lamnd.zerotohero.security.JwtUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepo userRepo;
    private final BlacklistTokenRepo blacklistTokenRepo;
    private final JwtUtil jwtUtil;
    private final OutboundConfig outboundConfig;
    private final OutboundIdentityClient outboundIdentityClient;
    private final OutboundUserClient outboundUserClient;

    public AuthResponse outboundAuthenticate(String code) {
        // exchange token with google by authorization code
        var response = outboundIdentityClient.exchangeToken(
                ExchangeTokenRequest.builder()
                        .code(code)
                        .clientId(outboundConfig.getClientId())
                        .clientSecret(outboundConfig.getClientSecret())
                        .redirectUri(outboundConfig.getRedirectUri())
                        .grantType(outboundConfig.getGrantType())
                        .build()
        );

        // get user info from google api
        var userInfo = outboundUserClient.getUserInfo("json", response.getAccessToken());

        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().name(ERole.USER.name()).build()); // assign default role to user

        // check if user is existing in database, if not we can create a new user
        var user = userRepo.findByUsername(userInfo.getEmail())
                // user is not existing in database, create new one
                .orElseGet(() -> {
                    return userRepo.save(User.builder()
                            .username(userInfo.getEmail())
                            .firstName(userInfo.getGivenName())
                            .lastName(userInfo.getFamilyName())
                            .roles(roles)
                            .build());
                });

        // generate jwt token for user
        String token = jwtUtil.generateToken(user);

        return AuthResponse.builder()
                .accessToken(token)
                .build();
    }

    public AuthResponse authenticate(AuthRequest authRequest) {
        var user = userRepo.findByUsername(authRequest.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_CREDENTIALS));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        if (!passwordEncoder.matches(authRequest.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.BAD_CREDENTIALS);
        }

        var token = jwtUtil.generateToken(user);

        return new AuthResponse(token);
    }

    public void logout(LogoutRequest logoutRequest) {
        try {
            SignedJWT signedToken = jwtUtil.verifyToken(logoutRequest.getToken(), false);

            blacklistToken(signedToken);
        } catch (JOSEException | ParseException e) {
            log.error("Cannot verify token: ", e);

            throw new JwtException(e.getMessage());
        }
    }

    public AuthResponse refreshToken(RefreshRequest refreshRequest) {
        try {
            SignedJWT signedToken = jwtUtil.verifyToken(refreshRequest.getToken(), true);

            blacklistToken(signedToken);

            String username = signedToken.getJWTClaimsSet().getSubject();
            var user = userRepo.findByUsername(username).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

            var newToken = jwtUtil.generateToken(user);

            return new AuthResponse(newToken);
        } catch (JOSEException | ParseException e) {
            log.error("Cannot verify token: ", e);

            throw new JwtException(e.getMessage());
        }
    }

    public IntrospectResponse introspect(IntrospectRequest introspectRequest) {
        var token = introspectRequest.getToken();
        boolean isValid = true;

        try {
            jwtUtil.verifyToken(token, false);
        } catch (JOSEException | ParseException e) {
            log.error("Cannot verify token: ", e);

            throw new JwtException(e.getMessage());
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().isValid(isValid).build();
    }

    private void blacklistToken(SignedJWT signedToken) throws ParseException {
        String jwtId = signedToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();

        BlacklistToken blacklistToken =
                BlacklistToken.builder().id(jwtId).expiryTime(expiryTime).build();

        blacklistTokenRepo.save(blacklistToken);
    }
}
