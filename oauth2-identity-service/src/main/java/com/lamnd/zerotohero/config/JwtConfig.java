package com.lamnd.zerotohero.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class JwtConfig {
    @Value("${jwt.signer_key}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.access_token_expiration}")
    private long accessTokenExpirationTime;

    @Value("${jwt.refresh_token_expiration}")
    private long refreshTokenExpirationTime;
}
