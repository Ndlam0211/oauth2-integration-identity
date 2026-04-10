package com.lamnd.zerotohero.security;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.lamnd.zerotohero.config.JwtConfig;
import com.lamnd.zerotohero.entity.User;
import com.lamnd.zerotohero.exception.AppException;
import com.lamnd.zerotohero.exception.ErrorCode;
import com.lamnd.zerotohero.repository.BlacklistTokenRepo;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    private final JwtConfig jwtConfig;
    private final BlacklistTokenRepo blacklistTokenRepo;

    public SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(jwtConfig.getSecretKey().getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        var verified = signedJWT.verify(verifier);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                        .getJWTClaimsSet()
                        .getIssueTime()
                        .toInstant()
                        .plus(jwtConfig.getRefreshTokenExpirationTime(), ChronoUnit.SECONDS)
                        .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        if (!verified) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        } else if (expiryTime.before(new Date())) {
            throw new AppException(ErrorCode.EXPIRED_TOKEN);
        } else if (blacklistTokenRepo.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.LOCKED_TOKEN);
        }

        return signedJWT;
    }

    public String generateToken(User user) {
        log.info("api key: {}", jwtConfig.getSecretKey());
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);

        Date expiryTime = new Date(Instant.now()
                .plus(jwtConfig.getAccessTokenExpirationTime(), ChronoUnit.SECONDS)
                .toEpochMilli());

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer(jwtConfig.getIssuer())
                .issueTime(new Date())
                .expirationTime(expiryTime)
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        try {
            jwsObject.sign(new MACSigner(jwtConfig.getSecretKey().getBytes()));

            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot generate token: ", e);
            throw new RuntimeException(e);
        }
    }

    public JWTClaimsSet getClaimsSet(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);

        return signedJWT.getJWTClaimsSet();
    }

    private String buildScope(User user) {
        StringJoiner scope = new StringJoiner(" ");

        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(role -> {
                scope.add("ROLE_" + role.getName());

                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> scope.add(permission.getName()));
            });
        }

        return scope.toString();
    }
}
